package com.zyq.frechwind.mp.controller;

import com.zyq.frechwind.base.AppException;
import com.zyq.frechwind.bean.User;
import com.zyq.frechwind.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Edward
 */
@AllArgsConstructor
@Controller
@RequestMapping("/wx/redirect/{appid}")
@Slf4j
public class WxRedirectController {
    private final WxMpService wxService;
    @Autowired
    private UserService userService;

    @GetMapping("/authUrl")
    public String authUrl(@PathVariable String appid, @RequestParam String url) {
        log.info("appid----=" + appid);
        log.info("url-----redirect----=" + url);
        url = this.wxService.switchoverTo(appid).oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        log.info("authUrl---------=" + url);
        return url;
    }

    @GetMapping("/bind-wechat-user")
    public String bindWechatUser(@PathVariable String appid, @RequestParam String code, HttpServletResponse response) {
        log.info("appid----=" + appid);
        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        log.info("code----1-----=" + code);
        try {
            WxMpOAuth2AccessToken accessToken = wxService.oauth2getAccessToken(code);
            WxMpUser user = wxService.oauth2getUserInfo(accessToken, null);

            //查找member，如果有，则自动登录。如果没有，则跳转到登陆页。
            String wechatOpenId = user.getOpenId();
            log.info("wechatOpenId----1-----=" + wechatOpenId);
            //根据openid查询member
            User u = userService.wechatCreate(user.getOpenId(),user.getNickname(), user.getHeadImgUrl());

            if (u == null) {
                log.info("wechatOpenId----2-----=" + wechatOpenId);
                String url = "http://39.97.213.25:8080/wx/redirect/"+appid+"//bind-wechat-user";
                return "redirect:/wx/redirect/"+appid+"/authUrl?url=" + url;
            } else {
                //自动登录，获取restSessionCode


                return "redirect:/blog/blog-list?userId=" + u.getUserId();
            }
        } catch (WxErrorException e) {
            throw new AppException(e);
        }
    }
}

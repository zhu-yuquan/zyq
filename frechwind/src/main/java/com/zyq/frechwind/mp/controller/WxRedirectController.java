package com.zyq.frechwind.mp.controller;

import com.zyq.frechwind.bean.User;
import com.zyq.frechwind.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/greet")
    public String greetUser(@PathVariable String appid, @RequestParam String code, ModelMap map) {
        log.info("code=" + code);
        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            WxMpOAuth2AccessToken accessToken = wxService.getOAuth2Service().getAccessToken(code);
            WxMpUser user = wxService.getOAuth2Service().getUserInfo(accessToken, null);
            log.info("----nickName=" + user.getNickname()+",openId=" + user.getOpenId());
            User u = userService.wechatCreate(user.getOpenId(), user.getNickname(), user.getHeadImgUrl());

            map.put("user", user);
            return "redirect:/blog/blog-list?userId=" + u.getUserId();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return "error";
    }
}

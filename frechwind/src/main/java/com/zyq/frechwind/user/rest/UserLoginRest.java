package com.zyq.frechwind.user.rest;

import com.zyq.frechwind.base.SwaggerDoc;
import com.zyq.frechwind.bean.Blog;
import com.zyq.frechwind.blog.service.BlogService;
import com.zyq.frechwind.pub.service.MessageSendService;
import io.swagger.annotations.Api;
import org.apache.catalina.Session;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "/rest-user", description = "用户信息")
@RequestMapping("/rest-user")
@SwaggerDoc
public class UserLoginRest {
    @Autowired
    private BlogService blogService;
    @Autowired
    private MessageSendService messageSendService;

    @GetMapping("/user-reg")
    public List<Blog> blogList(String userId,String email,String password){
        List<Blog> blogList = blogService.blogList(userId);
        return null;
    }

    @GetMapping("/send-email")
    public void sendMail(String email, HttpServletRequest request){
        //邮箱内容
        StringBuffer sb = new StringBuffer();
        String yzm = RandomStringUtils.randomNumeric(6);
        HttpSession session = request.getSession();
        session.setAttribute("verCode", yzm);

        sb.append("<!DOCTYPE>"+"<div bgcolor='#f1fcfa' style='border:1px solid #d9f4ee;font-size:14px;line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;   padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>"
                + "<div style='width:950px;font-family:arial;'>欢迎使用个人微博，您的注册码为：<br/><h2 style='color:green'>"+yzm+"</h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>个人微博网站</div>"
                +"</div>");
        try {
            messageSendService.sendSiteMessage("注册验证码",sb.toString(), email);
        } catch (MessagingException e) {
            System.out.println("send mail error:" + e.getMessage());
            e.printStackTrace();
        }
    }
}

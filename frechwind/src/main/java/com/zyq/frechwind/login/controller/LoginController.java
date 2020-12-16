package com.zyq.frechwind.login.controller;


import com.zyq.frechwind.base.SwaggerDoc;
import com.zyq.frechwind.bean.User;
import com.zyq.frechwind.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Api(value = "/login", description = "用户登录")
@RequestMapping(value = "/login")
@SwaggerDoc
public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * 账号密码登录
     * @param userName
     * @param passWord
     * @param model
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户登录成功，得到token")
    @PostMapping("/login")
    public String login(String userName, String passWord, Model model, HttpServletRequest request){

        User user = userService.userAccount(userName);
        if (user == null){
            model.addAttribute("message", "没有该账号请注册");
            return "/login/login";
        }
        user = userService.login(userName,passWord);
        if (user == null){
            model.addAttribute("message", "账号密码错误");
            return "/login/login";
        }
        model.addAttribute("user", user);
        return "redirect:/blog/blog-list?userId=" + user.getUserId();
    }

    /**
     *
     * @param userName
     * @param passWord
     * @param email
     * @param model
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @PostMapping("/register")
    public String register(String userName, String passWord, String email, Model model){

        User user = userService.create(userName,passWord,email);

        if (user == null){
            model.addAttribute("message", "注册失败");
            return "/login/reg";
        }
        model.addAttribute("user", user);
        return "redirect:/blog/blog-list?userId=" + user.getUserId();
    }
}

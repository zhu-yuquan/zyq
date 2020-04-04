package com.zyq.frechwind.controller;

import com.zyq.frechwind.bean.User;
import com.zyq.frechwind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user")
public class UserLoginCotroller {

    @Autowired
    private UserService userService;

    /**
     * 账号密码登录
     * @param userName
     * @param passWord
     * @param model
     * @return
     */
    @GetMapping("/login")
    public String login(String userName,String passWord,Model model){

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

        return "/main/index";
    }

    @GetMapping("/index")
    public String Hello(Model model){

        return "/index/index";
    }
}

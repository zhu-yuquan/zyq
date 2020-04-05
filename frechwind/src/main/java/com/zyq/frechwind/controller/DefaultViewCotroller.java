package com.zyq.frechwind.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultViewCotroller {
    private static Logger logger = LoggerFactory.getLogger(DefaultViewCotroller.class);
    @GetMapping("/")
    public String defaultView(Model model){
        logger.info("登录");
        return "/login/login";
    }

}

package com.zyq.frechwind.controller;

import com.zyq.frechwind.base.SwaggerDoc;
import com.zyq.frechwind.bean.Blog;
import com.zyq.frechwind.bean.User;
import com.zyq.frechwind.blog.service.BlogService;
import com.zyq.frechwind.service.TestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@Api(value = "/rest", description = "测试")
@RequestMapping(value = "/test")
@SwaggerDoc
public class TestCotroller {

    @Autowired
    private TestService testService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/ftl")
    public String TestFtlTemplate(Model model){

        Map map = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put("key=" + i, "value=" + i);
        }
        model.addAttribute("list", Arrays.asList("string1", "string2", "string3", "string4", "string5", "string6"));
        model.addAttribute("map", map);
        model.addAttribute("name", "   htTps://wWw.zHyD.mE   ");
        model.addAttribute("htmlText", "<span style=\"color: red;font-size: 16px;\">html内容</span>");
        model.addAttribute("num", 123.012);
        model.addAttribute("null", null);
        model.addAttribute("dateObj", new Date());
        model.addAttribute("bol", true);
        return "ftltemplate";
    }

    @GetMapping("/index")
    public String Hello(Model model){
//        testService.create();
        List<User> userList = testService.userList();

        model.addAttribute("userList", userList);
        List<String> numberList = testService.numberList();
        model.addAttribute("numberList", numberList);
        return "/index/index";
    }

    @GetMapping("/blog")
    public String blog(String title,String content,String userId,Model model){
        Blog blog = blogService.create(title,content,userId);
        List<Blog> blogList = blogService.blogList(userId);
        model.addAttribute("blogList", blogList);
        return "/index/index";
    }
}

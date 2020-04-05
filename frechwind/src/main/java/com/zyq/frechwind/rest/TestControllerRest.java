package com.zyq.frechwind.rest;

import com.zyq.frechwind.base.SwaggerDoc;
import com.zyq.frechwind.bean.Blog;
import com.zyq.frechwind.service.BlogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "/rest-test", description = "用户信息")
@RequestMapping("/rest-test")
@SwaggerDoc
public class TestControllerRest {
    @Autowired
    private BlogService blogService;

    @GetMapping("/blog")
    public String blog(String title, String content, String userId, Model model){
        Blog blog = blogService.create(title,content,userId);
        List<Blog> blogList = blogService.blogList(userId);
        model.addAttribute("blogList", blogList);
        return "/index/index";
    }
}

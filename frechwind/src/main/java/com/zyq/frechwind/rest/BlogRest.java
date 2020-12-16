package com.zyq.frechwind.rest;

import com.zyq.frechwind.base.SwaggerDoc;
import com.zyq.frechwind.bean.Blog;
import com.zyq.frechwind.blog.service.BlogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "/rest-blog", description = "博客")
@RequestMapping("/rest-blog")
@SwaggerDoc
public class BlogRest {
    @Autowired
    private BlogService blogService;

    @GetMapping("/blog-list")
    public List<Blog> blogList(String userId){
        List<Blog> blogList = blogService.blogList(userId);
        return blogList;
    }
}

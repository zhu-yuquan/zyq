package com.zyq.frechwind.user.rest;

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
@Api(value = "/rest-user", description = "用户信息")
@RequestMapping("/rest-user")
@SwaggerDoc
public class UserLoginRest {
    @Autowired
    private BlogService blogService;

    @GetMapping("/user-reg")
    public List<Blog> blogList(String userId,String email,String password){
        List<Blog> blogList = blogService.blogList(userId);
        return null;
    }
}

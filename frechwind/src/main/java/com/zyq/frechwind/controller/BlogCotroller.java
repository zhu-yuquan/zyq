package com.zyq.frechwind.controller;

import com.zyq.frechwind.base.SwaggerDoc;
import com.zyq.frechwind.bean.Blog;
import com.zyq.frechwind.bean.User;
import com.zyq.frechwind.service.BlogService;
import com.zyq.frechwind.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Api(value = "/blog", description = "用户博客")
@RequestMapping(value = "/blog")
@SwaggerDoc
public class BlogCotroller {

    @Autowired
    private BlogService blogService;

    /**
     * 用户博客列表
     * @param userId
     * @param model
     * @return
     */
    @ApiOperation(value = "用户博客列表", notes = "用户博客列表")
    @GetMapping("/blog-list")
    public String blogList(String userId, Model model){

        model.addAttribute("userId", userId);
        List<Blog> blogList = blogService.blogList(userId);
        model.addAttribute("blogList", blogList);
        return "/blog/index";
    }

    @ApiOperation(value = "查看博客", notes = "查看博客")
    @GetMapping("/blog-view")
    public String blogView(String blogId, Model model){
        Blog blog = blogService.view(blogId);
        model.addAttribute("blog", blog);
        return "/blog/blog-view";
    }
    @ApiOperation(value = "添加博客", notes = "添加博客")
    @GetMapping("/blog-add")
    public String blogAdd(String userId, Model model){
        model.addAttribute("userId", userId);
        return "/blog/blog-create";
    }
    @ApiOperation(value = "编辑博客", notes = "编辑博客")
    @GetMapping("/blog-edit")
    public String blogEdit(String blogId, Model model){
        Blog blog = blogService.view(blogId);
        model.addAttribute("blog", blog);
        return "/blog/blog-update";
    }


    @ApiOperation(value = "创建博客", notes = "创建博客")
    @GetMapping("/blog-create")
    public String blogCreate(String userId,String title, String content, Model model){
        Blog blog = blogService.create(title,content,userId);
        return "redirect:/blog/blog-list?userId=" + userId;
    }

    @ApiOperation(value = "更新博客", notes = "更新博客")
    @GetMapping("/blog-update")
    public String blogUpdate(String blogId,String title, String content, Model model){
        Blog blog = blogService.update(blogId,title,content);
        return "redirect:/blog/blog-list?userId=" + blog.getUserId();
    }

    @ApiOperation(value = "删除博客", notes = "删除博客")
    @GetMapping("/blog-delete")
    public String blogDelete(String blogId, Model model){
        Blog blog = blogService.delete(blogId);
        return "redirect:/blog/blog-list?userId=" + blogId;
    }

}

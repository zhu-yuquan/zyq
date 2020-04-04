package com.zyq.frechwind.service;

import com.zyq.frechwind.base.Finder;
import com.zyq.frechwind.bean.Blog;
import com.zyq.frechwind.dao.BlogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BlogService {

    @Autowired
    private BlogDao blogDao;


    public List<Blog> blogList(String userId){
        Finder finder = new Finder("Blog");
        finder.equal("userId", userId);
        finder.equal("delFlag", "N");
        return blogDao.find(finder);
    }

    public Blog create(String title,String content,String userId){
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setUserId(userId);
        blog.setDelFlag("N");
        return blogDao.create(blog);
    }

}

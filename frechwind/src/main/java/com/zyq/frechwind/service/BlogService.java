package com.zyq.frechwind.service;

import com.zyq.frechwind.base.Finder;
import com.zyq.frechwind.bean.Blog;
import com.zyq.frechwind.dao.BlogDao;
import com.zyq.frechwind.pub.bean.Upload;
import com.zyq.frechwind.pub.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BlogService {

    @Autowired
    private BlogDao blogDao;
    @Autowired
    private UploadService uploadService;


    public List<Blog> blogList(String userId){
        Finder finder = new Finder("Blog");
        finder.equal("userId", userId);
        finder.equal("delFlag", "N");
        finder.order("createTime", "desc");
        List<Blog> blogList = blogDao.find(finder);

        for (Blog blog:blogList) {
            List<Upload> uploadList = uploadService.list("blogImage",blog.getBlogId(),"Blog");
            blog.setUploadList(uploadList);
        }

        return blogList;
    }

    public Blog create(String title,String content,String userId){
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setUserId(userId);
        blog.setDelFlag("N");
        blog = blogDao.create(blog);

        List<Upload> uploadList = uploadService.list("blogImage",userId,"Blog");
        for (Upload upload : uploadList) {
            upload.setOwnerId(blog.getBlogId());
            uploadService.update(upload);
        }
        return blog;
    }

    public Blog update(String blogId,String title,String content){
        Blog blog = blogDao.get(blogId);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setDelFlag("N");
        blog = blogDao.update(blog);

        List<Upload> uploadList = uploadService.list("blogImage",blog.getUserId(),"Blog");
        for (Upload upload : uploadList) {
            upload.setOwnerId(blog.getBlogId());
            uploadService.update(upload);
        }

        return blog;
    }

    public Blog view(String blogId){
        Blog blog = blogDao.get(blogId);
        List<Upload> uploadList = uploadService.list("blogImage",blogId,"Blog");
        blog.setUploadList(uploadList);
        return blog;
    }

    public Blog delete(String blogId){
        Blog blog = blogDao.get(blogId);
        blog.setDelFlag("Y");
        return blogDao.update(blog);
    }

}

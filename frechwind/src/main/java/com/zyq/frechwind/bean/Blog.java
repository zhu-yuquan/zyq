package com.zyq.frechwind.bean;

import com.zyq.frechwind.base.TimeStamp;
import com.zyq.frechwind.pub.bean.Upload;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="cms_blog")
public class Blog extends TimeStamp {

    @Id
    @Column(name = "blog_id", nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String blogId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "del_flag")
    private String delFlag;

    @Transient
    private List<Upload> uploadList;

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public List<Upload> getUploadList() {
        return uploadList;
    }

    public void setUploadList(List<Upload> uploadList) {
        this.uploadList = uploadList;
    }
}

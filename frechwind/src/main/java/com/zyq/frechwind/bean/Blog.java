package com.zyq.frechwind.bean;

import com.zyq.frechwind.base.Auditable;
import com.zyq.frechwind.base.TimeStamp;
import com.zyq.frechwind.pub.bean.Upload;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="cms_blog")
@Data
public class Blog extends TimeStamp {

    @Id
    @Column(name = "blog_id", nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String blogId;

    @ApiModelProperty("上级帖子id。主贴的话，这个字段为0")
    @Column(name = "parent_blog_id")
    private String parentTopicId;

    @ApiModelProperty("顶级帖子id")
    @Column(name = "root_blog_id")
    private String rootTopicId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ApiModelProperty("发博客用户的id")
    @Column(name = "user_id")
    private String userId;

    @ApiModelProperty("回复数")
    @Column(name = "replay_num")
    private Integer replayNum;

    @ApiModelProperty("点赞数")
    @Column(name = "like_num")
    private Integer likeNum;

    @ApiModelProperty("阅读数")
    @Column(name = "read_num")
    private Integer readNum;

    @ApiModelProperty("显示层次。从0开始")
    @Column(name = "show_level")
    private Integer showLevel;

    @ApiModelProperty("主贴的顺序")
    @Column(name = "top_seq")
    private Integer topSeq;

    @ApiModelProperty("发布时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("最后回复时间")
    @Column(name = "last_reply_time")
    private Date lastReplyTime;

    @ApiModelProperty("枚举审核")
    @Column(name = "audit_flag")
    @Enumerated(EnumType.STRING)
    private Auditable.AuditFlag auditFlag;

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

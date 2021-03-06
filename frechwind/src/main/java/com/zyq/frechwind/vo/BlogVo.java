package com.zyq.frechwind.vo;

import com.zyq.frechwind.base.Auditable;
import com.zyq.frechwind.pub.bean.Upload;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@ApiModel("BlogVo")
@Data
public class BlogVo {

    private String blogId;

    @ApiModelProperty("上级帖子id。主贴的话，这个字段为0")
    private String parentTopicId;

    @ApiModelProperty("顶级帖子id")
    private String rootTopicId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("发博客用户的id")
    private String userId;

    @ApiModelProperty("回复数")
    private Integer replayNum;

    @ApiModelProperty("点赞数")
    private Integer likeNum;

    @ApiModelProperty("阅读数")
    private Integer readNum;

    @ApiModelProperty("显示层次。从0开始")
    private Integer showLevel;

    @ApiModelProperty("主贴的顺序")
    private Integer topSeq;

    @ApiModelProperty("发布时间")
    private Date createTime;

    @ApiModelProperty("最后回复时间")
    private Date lastReplyTime;

    @ApiModelProperty("枚举审核")
    @Enumerated(EnumType.STRING)
    private Auditable.AuditFlag auditFlag;

    private String delFlag;

    @Transient
    private List<Upload> uploadList;
}

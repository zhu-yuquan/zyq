package com.zyq.frechwind.pub.bean;

import com.zyq.frechwind.base.ApplicationContext;
import com.zyq.frechwind.base.TimeStamp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;
import java.util.Date;

/**
 * Created by hebo on 2015/12/19.
 * 文件表
 */
@ApiModel("文件表")
@Entity
@Table(name = "cms_upload")
public class Upload extends TimeStamp {

    @Id
    @ApiModelProperty("文件ID")
    @Column(name = "upload_id", nullable = false, insertable = true, updatable = false, length = 50)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String uploadId;

    /**
     * 文件绝对路径
     */
    @ApiModelProperty("文件绝对路径")
    @Column(name = "absolute_path", nullable = false)
    private String absolutePath;

    /**
     * 新文件名称
     */
    @ApiModelProperty("新文件名称")
    @Column(name = "new_file_name", nullable = false)
    private String newFileName;

    /**
     * 原文件名称
     */
    @ApiModelProperty("原文件名称")
    @Column(name = "old_file_name", nullable = false)
    private String fileName;

    /**
     * 文件上传时间
     */
    @ApiModelProperty("文件上传时间")
    @Column(name = "upload_time", nullable = false)
    private Date uploadTime;

    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    @Column(name = "size", nullable = false)
    private Long size;

    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型")
    @Column(name = "type", nullable = false)
    private String type;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @Column(name = "delete_flag", nullable = false)
    private String deleteFlag;

    /**
     * 所属类型
     */
    @ApiModelProperty("所属类型")
    @Column(name = "owner_type", nullable = false)
    private String ownerType;

    /**
     * 所属id
     */
    @ApiModelProperty("所属id")
    @Column(name = "owner_id", nullable = false)
    private String ownerId;


    @Column(name = "seq")
    private Integer seq;

    /**
     * 业务分类
     */
    @ApiModelProperty("业务分类")
    @Column(name = "biz_type")
    private String bizType;

    @Transient
    public String sizeExp() {
        return FileUtil.sizeExp(size);
    }

    @Transient
    public String preview() {
        if (type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("png") || type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("gif")) {
            return absolutePath;
        }
        String extImg = "other";
        if (type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx")) {
            extImg = "doc.png";
        }
        if (type.equalsIgnoreCase("xlsx")||type.equalsIgnoreCase("xls")) {
            extImg = "execl.png";
        }
        if (type.equalsIgnoreCase("pdf")||type.equalsIgnoreCase("PDF")) {
            extImg = "pdf.png";
        }
        if (type.equalsIgnoreCase("ppt")||type.equalsIgnoreCase("PPT")) {
            extImg = "ppt.png";
        }
        if (type.equalsIgnoreCase("txt")||type.equalsIgnoreCase("TXT")) {
            extImg = "txt.png";
        }
        if (type.equalsIgnoreCase("zip")) {
            extImg = "zip.png";
        }
        if (type.equalsIgnoreCase("rar")) {
            extImg = "rar.png";
        }
        return "../fileExt/" + extImg + "";
    }

    public File toFile() {
        String root = ApplicationContext.getInstance().getUploadRoot();
        File file = new File(root.substring(0, root.length() - "/upload".length()) + absolutePath);
        return file;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}

package com.zyq.frechwind.pub.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 文件属性
 */
public class FilePro {

    public static final String File_Ordinary = "ordinary";
    public static final String File_Folderz = "folder";

    private String fileName;//文件名
    private String srcFileName;//文件名
    private String fileType;//类型
    private Long fileSize;//大小
    private String filePath;//路径
    private Date lastModified;//最后修改日期
    private String fileFormat;//格式

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String sizeExp() {
        if (fileSize < 1024) {
            return fileSize + "";
        }
        if (fileSize < 1024 * 1024) {
            return new BigDecimal(fileSize / 1024.00).setScale(2, BigDecimal.ROUND_HALF_UP) + " k";
        }
        if (fileSize >= (1024 * 1024) ) {
            return new BigDecimal(fileSize / (1024.00 * 1024)).setScale(2, BigDecimal.ROUND_HALF_UP) + " M";
        }

        return "文件尺寸大小出现计算错误。";
    }

    public String getSrcFileName() {
        return srcFileName;
    }

    public void setSrcFileName(String srcFileName) {
        this.srcFileName = srcFileName;
    }
}

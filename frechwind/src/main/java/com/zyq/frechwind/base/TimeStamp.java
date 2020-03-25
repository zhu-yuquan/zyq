package com.zyq.frechwind.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class TimeStamp implements Serializable {

    /**
     * 创建人
     */

    @ApiModelProperty(hidden = true)
    @Column(updatable = false, name = "creator")
    @JsonIgnore
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @Column(updatable = false, name = "create_time")
    private Date createTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "modifier")
    @JsonIgnore
    private String modifier;
    /**
     * 最后修改时间
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "modify_time")
    private Date modifyTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "create_ip")
    @JsonIgnore
    private String createIp;

    @ApiModelProperty(hidden = true)
    @Column(name = "modify_ip")
    @JsonIgnore
    private String modifyIp;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public String getClassPath() {
        return this.getClass().getName();
    }
}
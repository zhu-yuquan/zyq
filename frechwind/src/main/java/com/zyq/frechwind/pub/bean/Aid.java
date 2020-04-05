package com.zyq.frechwind.pub.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zyq.frechwind.base.PojoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel("区域表省、市、县、镇（乡）、村")
@Entity
@Table(name = "aid",indexes = {
        @Index(columnList = "code"),
        @Index(columnList = "parent"),
        @Index(columnList = "parent, level")})
public class Aid {

    @ApiModelProperty("区域code")
    @Id
    @Column(name = "code", nullable = false, insertable = true, updatable = false, length = 32)
    private String code;

    @ApiModelProperty("区域名称")
    @Column(name = "name", nullable = false)
    private String name;


    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum AidLevel implements PojoEnum {
        region ("大区"), province("省"), city("市"), country("县"), town("乡"), village("村");

        private String title;

        private AidLevel(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public String getName() {
            return name();
        }

    }

    @ApiModelProperty("level省、市、县、镇（乡）、村")
    @Column(name = "level", length = 20)
    @Enumerated(EnumType.STRING)
    private AidLevel level;

    @ApiModelProperty("")
    @Column(name = "code_short", nullable = false)
    private String codeShort;

    @ApiModelProperty("")
    @Column(name = "parent", nullable = false, length = 32)
    private String parent;

    @Transient
    private List<Aid> childAidList = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AidLevel getLevel() {
        return level;
    }

    public void setLevel(AidLevel level) {
        this.level = level;
    }

    public String getCodeShort() {
        return codeShort;
    }

    public void setCodeShort(String codeShort) {
        this.codeShort = codeShort;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<Aid> getChildAidList() {
        return childAidList;
    }

    public void setChildAidList(List<Aid> childAidList) {
        this.childAidList = childAidList;
    }
}
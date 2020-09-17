package com.zyq.frechwind.bean;

import com.zyq.frechwind.base.TimeStamp;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="cms_user")
@Data
public class User extends TimeStamp {

    @Id
    @Column(name = "user_id", nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String userId;

    @Column(name = "user_name", length = 20)
    private String userName;

    @Column(name = "account", length = 20)
    private String account;

    @Column(name = "pass_word", length = 32)
    private String passWord;

    @Column(name = "openid", length = 32)
    private String openId;

    @Column(name = "nick_name", length = 32)
    private String nickName;

    @Column(name = "head_img_url", length = 200)
    private String headImgUrl;

    private String sexDesc;
    private Integer sex;
    private String language;
    private String city;
    private String province;
    private String country;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "del_flag", length = 2)
    private String delFlag;


}

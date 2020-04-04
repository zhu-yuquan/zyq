package com.zyq.frechwind.bean;

import com.zyq.frechwind.base.TimeStamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="cms_user")
public class User extends TimeStamp {

    @Id
    @Column(name = "user_id", nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "account")
    private String account;

    @Column(name = "pass_word")
    private String passWord;

    @Column(name = "email")
    private String email;

    @Column(name = "del_flag")
    private String delFlag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}

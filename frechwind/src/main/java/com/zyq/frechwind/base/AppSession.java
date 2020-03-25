package com.zyq.frechwind.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppSession {
    private String appSessionId;
    private String account;
    private String clientCode;//登录设备的设备号
    private Date createTime;//创建时间
    private Date lastsAccessTime;//最后访问时间
    private Map<String, Object> attributeMap = new HashMap();

    AppSession(String appSessionId) {
        this.appSessionId = appSessionId;
    }

    public String getAppSessionId() {
        return appSessionId;
    }

    public void setAppSessionId(String appSessionId) {
        this.appSessionId = appSessionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastsAccessTime() {
        return lastsAccessTime;
    }

    public void setLastsAccessTime(Date lastsAccessTime) {
        this.lastsAccessTime = lastsAccessTime;
    }

    public Object getAttribute(String name){
        return attributeMap.get(name);
    }

    public void setAttribute(String name, Object obj){
        attributeMap.put(name, obj);
    }

    public void removeAttribute(String name) {
        attributeMap.remove(name);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }
}
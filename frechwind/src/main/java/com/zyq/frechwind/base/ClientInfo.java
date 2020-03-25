package com.zyq.frechwind.base;

import javax.servlet.http.HttpServletRequest;

public class ClientInfo {
    private String loginIp;
    private String url;

    public ClientInfo(HttpServletRequest request) {
        this.loginIp = SecureUtil.getClientIP(request);
        this.url = request.getRequestURL().toString();
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
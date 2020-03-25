package com.zyq.frechwind.base;

public class AppException extends RuntimeException {
    public static final int ERR_CODE__NoLogin = 512;
    public static final int ERR_CODE__SensitiveError = 513;
    public static final int ERR_CODE__BLOCKED_IP = 514;
    public static final int ERR_CODE__XssConent=901;


    private String code;
    private String info;
    private String remark;
    private int responseCode = 0;
    private String expClassName = "AppException";//异常类的类名

    public AppException(Exception e) {
        super(e);
        this.info = e.toString();
        this.remark = e.getMessage();
        if(e instanceof AppException){
            AppException ae = (AppException)e;
            this.expClassName = ae.expClassName;
        }
    }

    public AppException(Throwable t) {
        super(t);
        info = t.toString();
        remark = t.getMessage();
    }

    public AppException(ErrorInfo errorInfo) {
        this.responseCode = errorInfo.getResponseCode();
        this.info = errorInfo.getTitle() + "[" + errorInfo.getName() + "]";
        this.code = errorInfo.getName();
    }

    public AppException(String info) {
        super(info);
        this.info = info;
        this.remark = info;
    }

    public AppException(int responseCode, String info) {
        super(info);
        this.info = info;
        this.responseCode = responseCode;
    }

    public AppException(String code, String message) {
        super(code);
        this.code = code;
        this.info = message;
    }

    public AppException(String code, String message, String remark) {
        super(code);
        this.code = code;
        this.info = message;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
//        return "[" + code + "]-[" + info + "]-[" + remark + "]";
        return remark;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getExpClassName() {
        return expClassName;
    }

    public void setExpClassName(String expClassName) {
        this.expClassName = expClassName;
    }
}
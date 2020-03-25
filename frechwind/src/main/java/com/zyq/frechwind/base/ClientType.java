package com.zyq.frechwind.base;

public enum ClientType implements PojoEnum {
    Pc("电脑"), Ios("苹果App"), Android("安卓App"), Wechat("微信"), MiniApp("小程序");

    private String title;

    private ClientType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name();
    }
}

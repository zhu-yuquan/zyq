package com.zyq.frechwind.base;

public enum ErrorInfo implements PojoEnum {

    //会员部分
    MemberNotLogin(512, "会员没有登录"),
    SensitiveError(513, "您提交的内容含有敏感词汇，请修改后再提交"),
    BLOCKED_IP(514, "IP被封"),
    NO_SESSION_CODE(515, "没有restSession"),
    DuplicateLogin(516, "不能重复登录。"),
    WECHAT_LOGIN_ERROR(531, "微信登录失败"),
    WECHAT_NONE_APPID(532, "没有配置AppId"),

    //商品部分
    ProductNotExist(601, "商品不存在"),
    ProductNoOnSale(602, "该商品已下架或其他原因暂时不能购买"),
    ProductUnderStock(603, "库存不足"),
    ProductNotSingleSku(604, "该商品不是只有一个SKU。"),
    //订单部分
    OrderNoCartItem(701, "订单没有选择商品"),
    OrderNoRecvAddress(702, "订单没有收货地址"),
    OrderInvoiceInfoError(703, "发票信息填写不完整"),

    //公共部分
    XssConent(901, "提交的内容不安全。");

    private ErrorInfo(int responseCode, String title){
        this.responseCode = responseCode;
        this.title = title;
    }

    private int responseCode = 0;
    private String title;

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name();
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
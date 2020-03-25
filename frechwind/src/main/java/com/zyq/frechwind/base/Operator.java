package com.zyq.frechwind.base;

/**
 * 系统操作人，目前有member和User两个实现
 */

public interface Operator {
    public String getOperatorId();

    public String getLastLoginIp();

    public String getName();

    public String getAvatar();
}

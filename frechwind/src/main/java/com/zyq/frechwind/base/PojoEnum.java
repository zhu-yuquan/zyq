package com.zyq.frechwind.base;

public interface PojoEnum<E extends Enum<E>> {
    public String getTitle();

    public String name();

    public String getName();

}

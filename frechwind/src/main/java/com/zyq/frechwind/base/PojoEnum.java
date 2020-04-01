package com.zyq.frechwind.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PojoEnumDeserializer.class)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface PojoEnum<E extends Enum<E>> {
    public String getTitle();

    public String name();

    public String getName();

}

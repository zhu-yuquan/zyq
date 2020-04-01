package com.zyq.frechwind.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2019-11-19.
 */
public class PojoEnumDeserializer extends JsonDeserializer<PojoEnum> {

    @Override
    @SuppressWarnings("unchecked")
    public PojoEnum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String currentName = jp.currentName();
        Object currentValue = jp.getCurrentValue();
        @SuppressWarnings("rawtypes")
        Class findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        JsonFormat annotation = (JsonFormat) findPropertyType.getAnnotation(JsonFormat.class);
        Enum valueOf;
        if (annotation == null || annotation.shape() != JsonFormat.Shape.OBJECT) {
            valueOf = valueOf(node.asText(), findPropertyType);
        } else {
            valueOf = valueOf(node.get("name").asText(), findPropertyType);
        }
        return (PojoEnum) valueOf;
    }

    static <E extends Enum<E> & PojoEnum> E valueOf(String enumCode,Class<E> clazz) {
        E enumm = Enum.valueOf(clazz, enumCode);
        return enumm;
    }

}
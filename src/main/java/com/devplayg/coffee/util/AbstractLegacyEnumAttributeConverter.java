package com.devplayg.coffee.util;


import com.devplayg.coffee.exception.ResourceNotFoundException;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.function.Supplier;

@Getter
public class AbstractLegacyEnumAttributeConverter<E extends Enum<E> & EnumModel> implements AttributeConverter<E, String> {
    private Class<E> enumClass;
    private boolean nullable;
    private String enumName;

    public AbstractLegacyEnumAttributeConverter(Class<E> enumClass, boolean nullable, String enumName) {
        this.enumClass = enumClass;
        this.nullable = nullable;
        this.enumName = enumName;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        if (!nullable && attribute == null) {
            throw new IllegalArgumentException(String.format("%s cannot be null", enumName));
        }
        return attribute.getCode();
    }

    @Override
    public E convertToEntityAttribute(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return EnumSet.allOf(this.enumClass).stream()
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException(this.enumClass.getName(), enumName, code));
    }

    static Supplier<ResourceNotFoundException> notFound(String msg) {
        return () -> new ResourceNotFoundException(msg, "a", "bbb");
    }
}
package com.devplayg.coffee.util;


import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Getter
public class AbstractLegacyEnumAttributeConverter<E extends Enum<E> & EnumModel> implements AttributeConverter<E, String> {
    private Class<E> targetEnumClass;
    private boolean nullable;
    private String enumName;

    public AbstractLegacyEnumAttributeConverter(boolean nullable, String enumName) {
//        this.targetEnumClass = targetEnumClass;
        this.nullable = nullable;
        this.enumName = enumName;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute.getCode();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        return null;
    }
}
//import javax.persistence.Converter;
//import com.devplayg.coffee.definition.AuditCategory;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.apache.commons.lang3.StringUtils;

//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;

//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.apache.commons.lang3.StringUtils;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@Converter(autoApply = true)
//@Converter
//public class AbstractLegacyEnumAttributeConverter implements AttributeConverter<AuditCategory, String> {
//
//    private Class<AuditCategory> targetEnumClass;
//
//    private boolean nullable;
//
//    private String enumName;
//
//    public AbstractLegacyEnumAttributeConverter(boolean nullable, String enumName) {
//        this.targetEnumClass = AuditCategory.class;
//        this.nullable = nullable;
//        this.enumName = enumName;
//    }
//
//    @Override
//    public String convertToDatabaseColumn(AuditCategory attribute) {
//        if (!nullable && attribute == null) {
//            throw new IllegalArgumentException(String.format("%s cannot be null", enumName));
//        }
//        return attribute.getCode();
//    }
//
//    @Override
//    public AuditCategory convertToEntityAttribute(String dbData) {
//        if (!nullable && StringUtils.isBlank(dbData)) {
//            throw new IllegalArgumentException(String.format("%s가 DB에 null 또는 빈 값으로 저장됨", enumName, dbData));
//        }
//
//        return CodeConvertingUtil.ofCode(targetEnumClass, dbData);
//        List<AuditCategory> list = Arrays.stream(AuditCategory.values())
//                                    .filter(item -> item.getCode().equals(attribute))
//                                    .collect(Collectors.toList());
//
//
//        return Arrays.stream(AuditCategory.values())
//                .filter(item -> item.getCode().equals(attribute))
//                .findFirst()
//                .orElseThrow(IllegalArgumentException::new);
//    }
//}


//@Converter
//public class AbstractLegacyEnumAttributeConverter<E extends Enum<E> & EnumCode>
//        implements AttributeConverter<E, String> {
//    private Class<E> targetEnumClass;
//    private boolean nullable;
//    private String enumName;
//
//    public AbstractLegacyEnumAttributeConverter(Class<E> targetEnumClass, boolean nullable, String enumName) {
//        this.targetEnumClass = targetEnumClass;
//        this.nullable = nullable;
//        this.enumName = enumName;
//    }
//
//    @Override
//    public String convertToDatabaseColumn(E attribute) {
//        if (!nullable && attribute == null) {
//            throw new IllegalArgumentException(String.format("%s cannot be null", enumName));
//        }
//        return CodeConvertingUtil.toCode(attribute);
//        return "xxl";
//    }
//
//    @Override
//    public E convertToEntityAttribute(String dbData) {
//        if (!nullable && StringUtils.isBlank(dbData)) {
//            throw new IllegalArgumentException(String.format("%s가 DB에 null 또는 빈 값으로 저장됨", enumName, dbData));
//        }
//        return CodeConvertingUtil.ofCode(targetEnumClass, dbData);
//        return null;
//    }
//}
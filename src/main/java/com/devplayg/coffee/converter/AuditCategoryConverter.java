package com.devplayg.coffee.converter;


import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.util.AbstractLegacyEnumAttributeConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//import com.devplayg.coffee.definition.AuditCategory;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
@Converter
public class AuditCategoryConverter extends AbstractLegacyEnumAttributeConverter<AuditCategory> {
    public AuditCategoryConverter() {
        super(false, "AuditCategory");
    }

//    @Override
//    public String convertToDatabaseColumn(AuditCategory attribute) {
//        return attribute.getCode();
//    }
//
//    @Override
//    public AuditCategory convertToEntityAttribute(String dbData) {
//        return null;
//    }

//    @Override
//    public Object convertToDatabaseColumn(Object attribute) {
//        return null;
//    }
//
//    @Override
//    public AuditCategory convertToEntityAttribute(Object dbData) {
//        return null;
//    }
}


//import com.devplayg.coffee.definition.AuditCategory;
//import com.devplayg.coffee.util.AbstractLegacyEnumAttributeConverter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Converter;
//
//@Converter
//public class AuditCategoryConverter extends AbstractLegacyEnumAttributeConverter {
//    public static final String ENUM_NAME = "Audit category";
//
//    public AuditCategoryConverter() {
//        super(false, ENUM_NAME);
//    }
//}


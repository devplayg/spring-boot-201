package com.devplayg.coffee.converter;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.util.AbstractLegacyEnumAttributeConverter;

import javax.persistence.Converter;

// http://woowabros.github.io/experience/2019/01/09/enum-converter.html

@Converter
public class AuditCategoryConverter extends AbstractLegacyEnumAttributeConverter<AuditCategory> {
    public AuditCategoryConverter() {
        super(AuditCategory.class, true, "AuditCategory");
    }
}

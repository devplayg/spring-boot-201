package com.devplayg.coffee.converter;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.util.AbstractLegacyEnumAttributeConverter;

import javax.persistence.Converter;

@Converter
public class AuditCategoryConverter extends AbstractLegacyEnumAttributeConverter<AuditCategory> {
    public AuditCategoryConverter() {
        super(AuditCategory.class, false, "AuditCategory");
    }
}

package com.devplayg.coffee.definition;


import com.devplayg.coffee.util.EnumModel;
import lombok.Getter;

@Getter
public enum AuditCategory implements EnumModel {
    MEMBER("this is user category", "0001"),
    SYSTEM("this is system category", "0002"),
    REPORT("this is report category", "0003"),
    DATABASE("this is database category", "0004");

    private String description;
    private String code;

    AuditCategory(String description, String code) {
        this.description = description;
        this.code = code;
    }
}
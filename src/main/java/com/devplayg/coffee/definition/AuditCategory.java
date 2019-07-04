package com.devplayg.coffee.definition;


import com.devplayg.coffee.util.EnumModel;
import lombok.Getter;

@Getter
public enum AuditCategory implements EnumModel {

    MEMBER_REMOVE("1011", ""),
    MEMBER_INSERT("1012", ""),
    MEMBER_UPDATE("1013", ""),

    LOGIN_SUCCESS("1021", ""),
    LOGIN_FAILED("1022", ""),
    LOGOUT("1023", "");

    private String description;
    private String code;

    AuditCategory(String code, String description) {
        this.description = description;
        this.code = code;
    }
}
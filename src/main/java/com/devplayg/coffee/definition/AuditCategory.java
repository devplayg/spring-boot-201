package com.devplayg.coffee.definition;


import com.devplayg.coffee.util.EnumModel;
import lombok.Getter;

@Getter
public enum AuditCategory implements EnumModel {

    MEMBER_CREATE("1011", ""),
    MEMBER_UPDATE("1012", ""),
    MEMBER_REMOVE("1013", ""),

    LOGIN_SUCCESS("1021", ""),
    LOGIN_FAILED("1022", ""),
    LOGOUT("1023", ""),

    APPLICATION_STARTED("1031", ""),
    APPLICATION_STOPPED("1032", "");

    private String description;
    private String code;

    AuditCategory(String code, String description) {
        this.description = description;
        this.code = code;
    }
}


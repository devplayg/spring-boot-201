package com.devplayg.coffee.definition;


import com.devplayg.coffee.util.EnumModel;
import lombok.Getter;

@Getter
public enum AuditCategory implements EnumModel {

    MEMBER_CREATE("1011", "Member created"),
    MEMBER_UPDATE("1012", "Member updated"),
    MEMBER_REMOVE("1013", "Member deleted"),

    LOGIN_SUCCESS("1021", "Logged in"),
    LOGIN_FAILED("1022", "Login failure"),
    LOGOUT("1023", "Logged out"),

    APPLICATION_STARTED("1031", "Application started"),
    APPLICATION_STOPPED("1032", "Application stopped");

    private String description;
    private String code;

    AuditCategory(String code, String description) {
        this.description = description;
        this.code = code;
    }
}

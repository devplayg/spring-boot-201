package com.devplayg.coffee.definition;

import com.devplayg.coffee.util.EnumModel;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class RoleType {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role implements EnumModel {
        ADMIN("Administrator"),
        SHERIFF("Sheriff"),
        USER("Normal user");

        private String description;
        //private String code;

        Role(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public String getCode() {
            return name();
        }
    }
}

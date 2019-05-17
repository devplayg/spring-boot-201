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

        private String value;

        Role(String value) {
            this.value = value;
        }

        @Override
        public String getKey() {
            return name();
        }

        @Override
        public String getValue() {
            return value;
        }
    }
}

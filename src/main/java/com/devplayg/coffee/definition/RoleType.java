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
        ADMIN("Administrator", (int) Math.pow(2, 10)), // 1024
        SHERIFF("Sheriff" , (int) Math.pow(2, 9)), // 512
        USER("Normal user" , (int) Math.pow(2, 8)); // 256

        private String description;
        private int value;
        //private String code;

        Role(String description, int value) {
            this.description = description;
            this.value = value;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public String getCode() {
            return name();
        }

        public int getValue() {
            return value;
        }
    }
}

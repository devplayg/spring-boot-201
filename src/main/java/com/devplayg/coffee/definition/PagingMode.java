package com.devplayg.coffee.definition;

import com.devplayg.coffee.util.EnumModel;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class PagingMode {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PagingMode paging;

    public enum Paging implements EnumModel {
        GeneralPaging("general-paging",1),
        FastPaging("fast-paging" , 2);

        private String description;
        private int value;

        Paging(String description, int value) {
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

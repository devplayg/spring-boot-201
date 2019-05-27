package com.devplayg.coffee.filter;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
//@Setter
@ToString
public class PagingFilter implements Serializable {
    private static final long serialVersionUID = 1L;
//    private static final long LIMIT = 100;

    private int offset;
    private int limit;
    private String sort;
    private String order;
    private boolean fastPaging;



//    public void setOffset(int offset) {
//        this.offset = (offset > 0) ? offset : 0;
//    }
//
//    public void setLimit(int limit) {
//        this.limit = (limit > LIMIT || limit < 0) ? 10 : limit;
//    }
}
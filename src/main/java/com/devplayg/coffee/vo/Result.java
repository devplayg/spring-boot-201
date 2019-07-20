package com.devplayg.coffee.vo;
//
//import com.querydsl.core.QueryResults;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@NoArgsConstructor
//@Getter
//public class Result {
//    private String error;
//    private long total;
//    private List rows;
//    private Object data;
//    private long lastInsertId;
//
//    public Result(String error) {
//        this.error = error;
//    }
//
//    public Result(long lastInsertId) {
//        this.lastInsertId = lastInsertId;
//    }
//    public Result(Object data) {
//        this.data = data;
//    }
//
//    public Result(List rows) {
//        this.rows = rows;
//        this.total = rows.size();
//    }
//
//    public Result(List rows, long total) {
//        this.rows = rows;
//        this.total = total;
//    }
//
//    public Result(BindingResult bindingResult) {
//        List<String> errors = new ArrayList<>();
//
//        for(FieldError e : bindingResult.getFieldErrors()){
//            errors.add(String.format("%s: %s", e.getField(), e.getDefaultMessage()));
//        }
//
//        this.error = String.join(",", errors);
//    }
//}

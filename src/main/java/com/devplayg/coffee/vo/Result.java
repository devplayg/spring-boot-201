package com.devplayg.coffee.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class Result {
    private int total;
    private List rows;
    private Object error;
    private long lastInsertID;

    public Result(String error) {
        this.error = error;
    }

    public Result(long id) {
        this.lastInsertID = id;
    }

    public Result(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();

        for(FieldError e : bindingResult.getFieldErrors()){
            errors.add(String.format("%s: %s", e.getField(), e.getDefaultMessage()));
        }
        this.error = errors;
    }

//    public DBResult(String error, List rows) {
//        this.error = error;
//        this.rows = rows;
//        this.total = (rows == null) ? 0 : rows.size();
//    }

//    public DBResult(String error, int total) {
//        this.error = error;
//        this.total = total; // affected rows
//    }

//    public DBResult(List rows, int total) {
//        this.rows = rows;
//        this.total = total;
//    }
}

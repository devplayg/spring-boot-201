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
    private String error;
    private int total;
    private List rows;
    private Object data;
    private long id;

    public Result(String error) {
        this.error = error;
    }

    public Result(long id) {
        this.id = id;
    }
    public Result(Object data) {
        this.data = data;
    }

    public Result(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();

        for(FieldError e : bindingResult.getFieldErrors()){
            errors.add(String.format("%s: %s", e.getField(), e.getDefaultMessage()));
        }

        this.error = String.join(",", errors);
    }
}

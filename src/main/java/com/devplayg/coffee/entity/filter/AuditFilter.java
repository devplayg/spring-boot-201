package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.definition.AuditCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class AuditFilter extends SearchFilter{

    private String message;

    private List<AuditCategory> categoryList = new ArrayList<>();

    private Boolean fastPaging;
}

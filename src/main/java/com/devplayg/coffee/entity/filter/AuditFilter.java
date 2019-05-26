package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.definition.AuditCategory;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class AuditFilter extends Paging {
    private String startDate;
    private String endDate;
    private String message;
    private List<AuditCategory> category;
    private String username;
}

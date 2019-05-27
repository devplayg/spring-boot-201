package com.devplayg.coffee.filter;

import com.devplayg.coffee.definition.AuditCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class AuditFilter extends PagingFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    private LocalDateTime now;

    private String message;
    private List<AuditCategory> categoryList = new ArrayList<>();

    @JsonIgnore
    public List<AuditCategory> getCategoryEnumList() {
        List<AuditCategory> list = new ArrayList<>();
        for (AuditCategory category : categoryList) {
            list.add(category);
        }
        return list;
    }

    public void check(String tz) {
        LocalDateTime now = ZonedDateTime.now(ZoneId.of(tz)).toLocalDateTime();
        this.now = now;
        if (this.startDate == null) {
            this.startDate = now.toLocalDate().atTime(LocalTime.MIN).minusSeconds(12345);
            this.endDate = now.toLocalDate().atTime(LocalTime.MAX);
        }
    }
}

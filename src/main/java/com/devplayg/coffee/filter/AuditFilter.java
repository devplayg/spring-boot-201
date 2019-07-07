package com.devplayg.coffee.filter;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.util.StringUtils;
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

    private final String sortName = "created";
    private final String sortOrder = "desc";

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    private LocalDateTime now;

    private String message;
    private List<AuditCategory> categoryList = new ArrayList<>();

    public void tune(ZoneId zoneId) {
        LocalDateTime now = ZonedDateTime.now(zoneId).toLocalDateTime();
        this.now = now;
        if (this.startDate == null) {
            this.startDate = now.toLocalDate().atTime(LocalTime.MIN).minusSeconds(12345);
        }
        if (this.endDate == null) {
            this.endDate = now.toLocalDate().atTime(LocalTime.MAX);
        }
        if (StringUtils.isBlank(this.getSort())) {
            this.setSort(sortName);
        }
    }
}

package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.framework.MembershipCenter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class AuditFilter {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    private String message;

    private List<AuditCategory> categoryList = new ArrayList<>();

    private Boolean fastPaging;

    public void tune() {
        LocalDateTime now = ZonedDateTime.now(MembershipCenter.getMemberTimezone()).toLocalDateTime();
        if (this.startDate == null) {
            this.startDate = now.toLocalDate().atStartOfDay();
        }
        if (this.endDate == null) {
            this.endDate = this.startDate.plusSeconds(86400 - 1);
        }
    }
}

package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.framework.InMemoryMemberManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
public class SearchFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    public void tune() {
        LocalDateTime now = ZonedDateTime.now(InMemoryMemberManager.getCurrentMemberTimezone()).toLocalDateTime();
        if (this.startDate == null) {
            this.startDate = now.toLocalDate().atStartOfDay();
        }
        if (this.endDate == null) {
            this.endDate = this.startDate.plusSeconds(86400 - 1);
        }
    }
}

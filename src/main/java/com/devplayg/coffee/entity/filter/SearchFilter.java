package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.framework.CustomPageRequest;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class SearchFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    private Boolean fastPaging;

    private CustomPageRequest pageable;

    public void tune(Pageable pageable, String defaultSortName, Sort.Direction defaultSortOrder) {
        LocalDateTime now = ZonedDateTime.now(InMemoryMemberManager.getCurrentMemberTimezone()).toLocalDateTime();
        if (this.startDate == null) {
            this.startDate = now.toLocalDate().atStartOfDay();
        }

        if (this.endDate == null) {
            this.endDate = this.startDate.plusSeconds(86400 - 1);
        }

        if (fastPaging == null) {
            fastPaging = false;
        }

        this.pageable = new CustomPageRequest(pageable, defaultSortName, defaultSortOrder);
    }
}

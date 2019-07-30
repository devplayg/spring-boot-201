package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.definition.PagingMode;
import com.devplayg.coffee.framework.CustomPageRequest;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
class SearchFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    private int pagingMode;

    private CustomPageRequest pageable;

    SearchFilter() {
        LocalDateTime now = ZonedDateTime.now(InMemoryMemberManager.getCurrentMemberTimezone()).toLocalDateTime();
        this.startDate = now.toLocalDate().atStartOfDay();
        this.endDate = this.startDate.plusSeconds(86400 - 1);
        pagingMode = PagingMode.Paging.GeneralPaging.getValue();
    }

    void tune(CustomPageRequest customPageRequest) {
        this.pageable = customPageRequest;
    }
}

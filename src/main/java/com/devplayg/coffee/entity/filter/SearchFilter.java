package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.definition.PagingMode;
import com.devplayg.coffee.framework.CustomPageRequest;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

@Getter
@Setter
@ToString
@Slf4j
public class SearchFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    // Time with zone
    @JsonIgnore
    private ZonedDateTime zoneStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    // Time with zone
    @JsonIgnore
    private ZonedDateTime zoneEndDate;

    private int pagingMode;

    @JsonIgnore
    private CustomPageRequest pageable;

    @JsonIgnore
    private ZoneId clientZoneId;

    SearchFilter() {
        pagingMode = PagingMode.Paging.GeneralPaging.getValue();
        clientZoneId = InMemoryMemberManager.getCurrentMemberTimezone();
    }

    @JsonIgnore
    public LocalDateTime getLocalizedStartdate() {
        return zoneStartDate.withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime();
    }

    @JsonIgnore
    public LocalDateTime getLocalizedEndDate() {
        return zoneEndDate.withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime().plusSeconds(59);
    }

    void tune(CustomPageRequest customPageRequest) {
        if (this.startDate == null) {
            ZonedDateTime now = ZonedDateTime.now(clientZoneId);
            this.startDate = now.toLocalDate().atStartOfDay(); // User's start time of the day
            this.endDate = this.startDate.plusSeconds(86400 - 1); // User's last time of the day (23:59:00 to 23:59:59)
        }

        // Set the time with user's timezone
        this.zoneStartDate = this.startDate.atZone(clientZoneId);
        this.zoneEndDate = this.endDate.atZone(clientZoneId);

        // Set custom pageable
        this.pageable = customPageRequest;
    }

    void setSearchDateRange(int days) {
        ZonedDateTime now = ZonedDateTime.now(getClientZoneId());
        setStartDate(now.minusDays(days).toLocalDate().atStartOfDay());
        setEndDate(now.toLocalDate().atStartOfDay().plusSeconds(86400 - 1));
    }
}

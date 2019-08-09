package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.definition.PagingMode;
import com.devplayg.coffee.framework.CustomPageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Slf4j
public class AuditFilter extends SearchFilter {
    private final static String defaultSortName = "id";
    private final static Sort.Direction defaultSortOrder = Sort.Direction.DESC;

    // Message
    private String message;

    // Category
    private List<AuditCategory> categoryList;

    // IP
    private String ip;

    public AuditFilter() {
        categoryList = new ArrayList<>();

        // Default fast paging mode
        setPagingMode(PagingMode.Paging.FastPaging.getValue());
    }

    public void tune(Pageable pageable) {
        if (getStartDate() == null) {
            ZonedDateTime now = ZonedDateTime.now(getClientZoneId());
            setStartDate(now.toLocalDate().atStartOfDay());
            setEndDate(getStartDate().plusSeconds(86400 - 1));
        }

        super.tune(new CustomPageRequest(pageable, defaultSortName, defaultSortOrder));
    }
}

package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.definition.PagingMode;
import com.devplayg.coffee.framework.CustomPageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class FactoryEventFilter extends SearchFilter {
    private final static String defaultSortName = "date";
    private final static Sort.Direction defaultSortOrder = Sort.Direction.DESC;

    private List<Long> factoryIdList;
    private List<Integer> eventTypeList;
    private List<Long> cameraList;
    private String viewMode;
    private int hour;
    private boolean today;

    public FactoryEventFilter() {
        this.factoryIdList = new ArrayList<>();
        this.eventTypeList = new ArrayList<>();
        this.cameraList = new ArrayList<>();
        this.setPagingMode(PagingMode.Paging.FastPaging.getValue());
        this.hour = -1;
    }

    public void tune(Pageable pageable) {
        if (this.getStartDate() == null) {
            if (this.today) {
                setSearchDateRange(0);
            } else {
                setSearchDateRange(7);
            }
        }
        super.tune(new CustomPageRequest(pageable, defaultSortName, defaultSortOrder));
    }
}

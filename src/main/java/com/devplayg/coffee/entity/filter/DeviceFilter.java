package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.framework.CustomPageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceFilter extends SearchFilter {
    private final static String defaultSortName = "username";
    private final static Sort.Direction defaultSortOrder = Sort.Direction.ASC;

    private int category;
    private int type;

    public void tune(Pageable pageable) {
        this.setFastPaging(false);
        super.tune(new CustomPageRequest(pageable, defaultSortName, defaultSortOrder));
    }
}

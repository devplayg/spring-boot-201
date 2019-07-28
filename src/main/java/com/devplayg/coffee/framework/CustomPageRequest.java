package com.devplayg.coffee.framework;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@ToString
public class CustomPageRequest {

    private Pageable delegate;
    private String sortName;
    private Sort.Direction sortOrder;

    public CustomPageRequest(Pageable pageable, String defaultSortName, Sort.Direction defaultSortOrder) {
        this.delegate = pageable;

        // If user requested sorting
        if (pageable.getSort().isSorted()) {
            if (pageable.getSort().iterator().hasNext()) {
                Sort.Order order = pageable.getSort().iterator().next();
                this.sortName = order.getProperty();
                this.sortOrder = order.getDirection();
            }
        } else {
            this.sortName = defaultSortName;
            this.sortOrder = defaultSortOrder;
        }

    }
}

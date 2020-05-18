package com.devplayg.coffee.entity.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NewsFilter {
    private long lastFactoryEventId;
    private int size;
    private List<Long> factoryIdList;
//    private List<Integer> eventTypeList;

    public NewsFilter() {
        this.size = 10;
        this.factoryIdList = new ArrayList<>();
//        this.eventTypeList = new ArrayList<>();
    }
}

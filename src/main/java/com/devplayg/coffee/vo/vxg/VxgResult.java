package com.devplayg.coffee.vo.vxg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class VxgResult {
    private Meta meta;
    private List<VxgChannel> objects;

    public VxgResult() {
        meta = new Meta();
        objects = new ArrayList<>();
    }

    @Getter
    @ToString
    public class Meta {
        @JsonProperty("total_count")
        private long totalCount;
    }
}

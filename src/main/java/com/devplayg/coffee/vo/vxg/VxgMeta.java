package com.devplayg.coffee.vo.vxg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VxgMeta {
    @JsonProperty("total_count")
    private long totalCount;
}
package com.devplayg.coffee.vo.vxg;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class VxgChannelsResult {
    private VxgMeta meta;
    private List<VxgChannel> objects;

    public VxgChannelsResult() {
        meta = new VxgMeta();
        objects = new ArrayList<>();
    }
}

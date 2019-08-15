package com.devplayg.coffee.vo.vxg;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class VxgImageResult {
    private VxgMeta meta;
    private List<VxgImage> objects;

    public VxgImageResult() {
        meta = new VxgMeta();
        objects = new ArrayList<>();
    }
}



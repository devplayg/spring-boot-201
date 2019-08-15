package com.devplayg.coffee.vo.vxg;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class VxgImage {
    private LocalDateTime time;
    private LocalDateTime start;
    private LocalDateTime end;
    private String url;
    private int width;
    private int height;
    private long size;
}

package com.devplayg.coffee.vo.vxg;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VxgWatchUrls {
    private String rtsp;
    private String hls;
    private String rtmp;
    private String rtc;
    private LocalDateTime expire;
}

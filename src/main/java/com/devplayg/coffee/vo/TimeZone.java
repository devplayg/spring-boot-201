/*
 * Developed by Won.
 * Last modified 19. 5. 20 오후 5:49.
 * Copyright (c) 2019. All rights reserved.
 */

package com.devplayg.coffee.vo;

import lombok.Getter;

@Getter
public class TimeZone {
    private String id;
    private String offset;

    public TimeZone(String id, String offset) {
        this.id = id;
        this.offset = offset;
    }
}

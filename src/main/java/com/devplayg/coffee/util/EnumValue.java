/*
 * References
 *      http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html
 */

package com.devplayg.coffee.util;

import lombok.Getter;

@Getter
public class EnumValue {
    private String code;
    private String description;

    public EnumValue(EnumModel enumModel) {
        this.code = enumModel.getCode();
        this.description = enumModel.getDescription();
    }
}
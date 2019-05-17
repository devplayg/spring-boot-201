/*
 * References
 *      http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html
 */

package com.devplayg.coffee.util;

import lombok.Getter;

@Getter
public class EnumValue {
    private String key;
    private String value;

    public EnumValue(EnumModel enumModel) {
        key = enumModel.getKey();
        value = enumModel.getValue();
    }
}
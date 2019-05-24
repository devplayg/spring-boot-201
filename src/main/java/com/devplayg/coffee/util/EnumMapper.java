/*
 * References
 *      http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html
 */

package com.devplayg.coffee.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumMapper {
    private Map<String, List<EnumValue>> factory = new HashMap<>();

    private List<EnumValue> toEnumValues(Class<? extends EnumModel> e) {
        return Arrays
                .stream(e.getEnumConstants())
                .map(EnumValue::new)
                .collect(Collectors.toList());
    }

    public void put(String name, Class<? extends EnumModel> e) {
        factory.put(name, toEnumValues(e));
    }

    public Map<String, List<EnumValue>> get(String names) {
        return Arrays
                .stream(names.split(","))
                .collect(Collectors.toMap(Function.identity(), name -> factory.get(name)));
    }

    public Map<String, List<EnumValue>> getAll() {
        return factory;
    }
}
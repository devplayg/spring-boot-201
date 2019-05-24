package com.devplayg.coffee.util;
//
//import com.devplayg.coffee.exception.ResourceNotFoundException;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.EnumSet;
//
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public class CodeConvertingUtil {
//    public static <T extends Enum<T> & EnumCode> T ofCode(Class<T> enumClass, String code) {
//        if (StringUtils.isBlank(code)) {
//            return null;
//        }
//
//        return EnumSet.allOf(enumClass).stream()
//                .filter(v -> v.getCode().equals(code))
//                .findAny()
//                .orElseThrow(() -> new ResourceNotFoundException(enumClass.getName(), enumClass.getName(), code));
//    }
//
//    public static <T extends Enum<T> & EnumCode> String toCode(T enumValue) {
//        if (enumValue == null) {
//            return "";
//        }
//
//        return enumValue.getCode();
//    }
//}
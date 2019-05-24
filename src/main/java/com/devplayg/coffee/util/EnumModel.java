/*
 * References
 *      http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html
 */

package com.devplayg.coffee.util;

public interface EnumModel {
    // --------------   -------------------  -------------
    // STATUS_RUNNING  ("System is running", "STATUS-0003")
    // --------------   -------------------  -------------
    //           name          description           code
    // --------------   -------------------  -------------
    //String getName(); // you don't need to set 'name'. java.lang.enum supports 'name'
    String getDescription();
    String getCode();
}
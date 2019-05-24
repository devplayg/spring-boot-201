package com.devplayg.coffee.definition;


import com.devplayg.coffee.util.EnumModel;
import lombok.Getter;

@Getter
public enum AuditCategory implements EnumModel {
    MEMBER("this is user category", "0001"),
    SYSTEM("this is system category", "0002");

    private String description;
    private String code;

    AuditCategory(String description, String code) {
        this.description = description;
        this.code = code;
    }
}



//public enum AuditCategory implements EnumModel{
//    MEMBER("RE", "1")
//    MEMBER("User", "0001"),
//    SYSTEM("System", "0002");

//    private String name;
//    private String description;
//    private String code;
//
//    AuditCategory(String name, String description, String code) {
//        this.name = name;
//        this.description = description;
//        this.code = code;
//    }
//}

//import com.devplayg.coffee.util.EnumModel;
//
//import javax.persistence.Column;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//
//public class AuditCategory {
//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private Category category;
//
//    private
//
//    public enum Category implements EnumModel {
//        MEMBER("User", "0001"),
//        SYSTEM("System", "0002");
//
//        private String value;
//
//        Category(String value) {
//            this.value = value;
//        }
//
//        @Override
//        public String getKey() {
//            return name();
//        }
//
//        @Override
//        public String getValue() {
//            return value;
//        }
//    }
//}


//import com.devplayg.coffee.util.EnumCode;
//import lombok.Getter;
//
//@Getter
//public enum AuditCategory implements EnumCode {
//    MEMBER("User", "0001"),
//    SYSTEM("System", "0002");
//
//    private String code;
//    private String name;
//
//    AuditCategory(String name, String code) {
//        this.code = code;
//        this.name = name;
//    }
//}
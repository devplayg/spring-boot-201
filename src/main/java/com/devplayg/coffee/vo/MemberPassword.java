package com.devplayg.coffee.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString // will be removed
public class MemberPassword {
    private String password;
    private String passwordConfirm;
}

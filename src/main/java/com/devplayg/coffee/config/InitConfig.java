package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.util.EnumMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitConfig {
    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("role", RoleType.Role.class);
        return enumMapper;
    }
}

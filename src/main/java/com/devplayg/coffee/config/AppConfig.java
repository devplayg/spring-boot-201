package com.devplayg.coffee.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ToString
@ConfigurationProperties("app")
public class AppConfig {
    private String homeUri;
    private String name;
    private List<String> pathPatternsNotToBeIntercepted;
    private AdminInfo adminInfo;

    public AppConfig() {
        adminInfo = new AdminInfo();
    }

    @Getter
    @Setter
    @ToString
    public class AdminInfo {
        private String name;
        private String email;
    }
}


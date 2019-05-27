package com.devplayg.coffee.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ToString
@ConfigurationProperties("app-config")
public class AppConfig {
    private DateFormat dateFormat = new DateFormat();

    @Getter @Setter
    public class DateFormat {
        private String datetimePicker;
        private String thymeleaf;
    }

}

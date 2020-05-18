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
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String homeUri;
    private String name;
    private AdminInfo adminInfo;
    private Boolean useIpBlocking;
    private List<String> pathPatternsNotToBeIntercepted;
    private String fileUploadDir;


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

    @Getter
    @Setter
    @ToString
    public static class Server {
        private long id;
        private String name;
        private String hostname;
        private String port;
        private String securePort;
        private Boolean secure;
        private String apiKey;
        private String url;
    }
}

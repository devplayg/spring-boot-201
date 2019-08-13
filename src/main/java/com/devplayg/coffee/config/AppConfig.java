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
    private Boolean useIpBlocking;
    private Server videoServer;
    private String fileUploadDir;

    public AppConfig() {
        adminInfo = new AdminInfo();
        videoServer = new Server();
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
    public class Server {
        private String address;
        private String apiKey;
    }
}


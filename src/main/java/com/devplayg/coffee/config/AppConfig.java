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
    private VmsServer vmsServer;
//    private Statistics statistics;

//    private FaceRecognition faceRecognition;

    // VMS
//    private String

    // NVR
//    private Server nvrServer;
//    private Server nvrAgent;

    // VAS
//    private List<Server> vasServer;
//    private Server vasAgent;

    // VMS
//    private Server vmsAgent;

    public AppConfig() {
//        faceRecognition = new FaceRecognition();
        adminInfo = new AdminInfo();
        vmsServer = new VmsServer();
//        statistics = new Statistics();

//        nvrServer = new Server();
//        nvrAgent = new Server();

//        vasServer = new ArrayList<>();
//        vasAgent = new Server();

//        vmsAgent = new Server();
    }


//    @ToString
//    @Setter
//    @Getter
//    public class FaceRecognition {
//        private String name;
//        private String logImageDir;
//        private String faceImageDir;
//    }

    @Getter
    @Setter
    @ToString
    public static class VmsServer {
        private String reportDir;
    }

    @Getter
    @Setter
    @ToString
    public class AdminInfo {
        private String name;
        private String email;
    }

//    @Getter
//    @Setter
//    @ToString
//    public class Statistics {
//        private String timezone;
//    }

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

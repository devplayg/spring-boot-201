package com.devplayg.coffee.vo.vxg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class VxgChannel {

    @JsonProperty("access_tokens")
    private AccessTokens accessTokens;

    private LocalDateTime created;
    private Long id;
    private String name;

    @JsonProperty("rec_mode")
    private String recMode;

    @JsonProperty("relay_to")
    private List<String> relayTo;
    private Boolean streaming;
    private String timezone;

    private Source source;

    public VxgChannel() {
        source = new Source();
        accessTokens = new AccessTokens();
        relayTo = new ArrayList<>();
    }


    @Getter
    @ToString
    @NoArgsConstructor
    public class AccessTokens {
        private String all;
        private String watch;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public class Source {
        private String login;
        private String password;
        private String type;
        private String url;
    }

}

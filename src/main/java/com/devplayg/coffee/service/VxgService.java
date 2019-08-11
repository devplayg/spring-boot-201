package com.devplayg.coffee.service;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.vo.vxg.VxgChannel;
import com.devplayg.coffee.vo.vxg.VxgResult;
import com.devplayg.coffee.vo.vxg.VxgSnapshot;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@Slf4j
public class VxgService {

    private final AppConfig appConfig;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public VxgService(AppConfig appConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Returns an array of all channels. A filter can be applied to retrieve a subset of channels.
     */
    public VxgResult getChannels() {
        VxgResult result = null;
        try {
            result = this.request("v3/channels/", VxgResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Gets description and basic settings of the channel
     */
    public VxgChannel getChannel(Long channelId) {
        VxgChannel channel = null;
        try {
            channel = this.request("v3/channels/" + channelId.toString() + "/", VxgChannel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }

    /**
     * Returns a snapshot image for a ‘pull’ channel if it exists. Requires a “watch” access-token.
     */

    public VxgSnapshot getSnapshot() {
        VxgSnapshot snapshot = null;
        try {
            snapshot = this.request("v4/live/image/", VxgSnapshot.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return snapshot;

//        String url = this.createTargetUrl("v3/channels/" + channelId.toString() + "/");
//        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(this.getHttpHeader()), String.class);
//        VxgChannel channel = objectMapper.readValue(result.getBody(), VxgChannel.class);
    // curl -X GET "https://127.0.0.1:86/api/v4/live/image/"
    // -H "accept: application/json"
    // -H "Authorization: Acc eyJ0b2tlb"
    }
    private <T> T request(String api, Class<T> valueType) throws IOException {
        String url = this.createTargetUrl(api);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(this.getHttpHeader()), String.class);
        return objectMapper.readValue(result.getBody(), valueType);
    }


    /**
     * Utils
     */
    private HttpHeaders getHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "LKey " + appConfig.getVideoServer().getApiKey());
        headers.add("accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private String createTargetUrl(String api) {
        return appConfig.getVideoServer().getAddress() + api;
    }

}
//        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(headers);
//        Map<String, String> params = new HashMap<>();
//        params.put("name", "jaeyeon");

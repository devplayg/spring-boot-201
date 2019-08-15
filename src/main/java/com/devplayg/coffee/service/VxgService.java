package com.devplayg.coffee.service;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.entity.Device;
import com.devplayg.coffee.repository.camera.CameraRepository;
import com.devplayg.coffee.vo.vxg.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class VxgService {

    private final AppConfig appConfig;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final CameraRepository cameraRepository;

    public VxgService(AppConfig appConfig, RestTemplate restTemplate, ObjectMapper objectMapper, CameraRepository cameraRepository) {
        this.appConfig = appConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.cameraRepository = cameraRepository;
    }

    /**
     * Returns an array of all channels. A filter can be applied to retrieve a subset of channels.
     */
    public VxgChannelsResult getChannels() {
        VxgChannelsResult result = null;
        try {
            result = this.requestViaAdminAPI("v3/channels/", VxgChannelsResult.class);
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
            channel = this.requestViaAdminAPI("v3/channels/" + channelId.toString() + "/", VxgChannel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }

    /**
     * Returns array of images that match specified criterion. Requires “watch” access-token.
     */
    public VxgImageResult getImages(long assetId, String uid, Pageable pageable) {
        Device device = cameraRepository.findOneByAssetIdAndUid(assetId, uid);
        VxgImageResult result = null;

        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        String url = String.format("v4/storage/images/?offset=%d&limit=%d&order_by=-time", offset, limit);
        try {
            result = this.requestViaChannelAPI(url, device.getApiKey(), VxgImageResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns live video URLs for the channel. This call must precede each attempt to play a live stream. Requires a “watch” access-token.
     */
    public VxgWatchUrls getWatchUrls(long assetId, String uid) {
        Device device = cameraRepository.findOneByAssetIdAndUid(assetId, uid);
        VxgWatchUrls result = null;
        try {
            result = this.requestViaChannelAPI("v4/live/watch/", device.getApiKey(), VxgWatchUrls.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns a snapshot image for a ‘pull’ channel if it exists. Requires a “watch” access-token.
     */
    public VxgSnapshot getSnapshot(long assetId, String uid) {
        Device device = cameraRepository.findOneByAssetIdAndUid(assetId, uid);
        VxgSnapshot snapshot = null;
        try {
            snapshot = this.requestViaChannelAPI("v4/live/image/", device.getApiKey(), VxgSnapshot.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return snapshot;
    }

    /**
     * Synchronize database with VXG
     */
    public Boolean sync(long assetId) {
        VxgChannelsResult result = this.getChannels();
        if (result.getMeta().getTotalCount() < 1) {
            return false;
        }

        List<Device> cameras = new ArrayList<>();
        for (VxgChannel ch : result.getObjects()) {

            Device camera = Device.builder()
                    .assetId(assetId)
                    .uid(ch.getId().toString())
                    .code(ch.getId()) // Camera ID
                    .name(ch.getName())
                    .apiKey(ch.getAccessTokens().getAll())
                    .created(ch.getCreated())
                    .username(ch.getSource().getLogin())
                    .password(ch.getSource().getPassword())
                    .url(ch.getSource().getUrl())
                    .timezone(ch.getTimezone())
                    .enabled(ch.getStreaming())
                    .status(ch.getRecMode())
                    .firmware("")
                    .hostname("")
                    .manufacturedBy("")
                    .model("")
                    .version("")
                    .build();

            cameras.add(camera);
        }

        cameraRepository.saveAll(cameras);
        return true;
    }


    /**
     * Request
     */
    private <T> T requestViaAdminAPI(String api, Class<T> valueType) throws IOException {
        String url = this.createTargetUrl(api);
        ResponseEntity<String> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(this.getHttpHeaderToUserForAdminAPI()),
                String.class
        );
        return objectMapper.readValue(result.getBody(), valueType);
    }

    private <T> T requestViaChannelAPI(String api, String apiKey, Class<T> valueType) throws IOException {
        String url = this.createTargetUrl(api);
        ResponseEntity<String> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(this.getHttpHeaderToUserForChannelAPI(apiKey)),
                String.class
        );
        return objectMapper.readValue(result.getBody(), valueType);
    }

    /**
     * Utils
     */
    private HttpHeaders getHttpHeaderToUserForAdminAPI() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "LKey " + appConfig.getVideoServer().getApiKey());
        headers.add("accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private HttpHeaders getHttpHeaderToUserForChannelAPI(String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Acc " + apiKey);
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

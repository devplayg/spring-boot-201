package com.devplayg.coffee.controller;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.entity.CameraPolicy;
import com.devplayg.coffee.entity.Device;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.repository.camera.CameraRepository;
import com.devplayg.coffee.repository.camerapolicy.CameraPolicyRepository;
import com.devplayg.coffee.service.VxgService;
import com.devplayg.coffee.vo.vxg.VxgImageResult;
import com.devplayg.coffee.vo.vxg.VxgSnapshot;
import com.devplayg.coffee.vo.vxg.VxgWatchUrls;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("cameras")
@Slf4j
public class CameraController {
    private static long assetId = 2; // 태창이엔지, 제1공장

    private final CameraRepository cameraRepository;

    private final CameraPolicyRepository cameraPolicyRepository;

    private final VxgService vxgService;

//    private final FactoryEventRepository factoryEventRepository;
//
//    private final FactoryEventRepositoryImpl factoryEventRepositoryImpl;

    private final AppConfig appConfig;

    private final RestTemplate restTemplate;

    public CameraController(CameraRepository cameraRepository, VxgService vxgService, CameraPolicyRepository cameraPolicyRepository, AppConfig appConfig, RestTemplate restTemplate) {
        this.cameraRepository = cameraRepository;
        this.vxgService = vxgService;
        this.cameraPolicyRepository = cameraPolicyRepository;
        this.appConfig = appConfig;
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String display(Model model) {
        model.addAttribute("cameras", cameraRepository.findAllByAssetIdOrderByNameAsc(assetId));
        model.addAttribute("assetId", assetId);
        return "camera/camera";
    }

    @RequestMapping(value = "settings/", method = {RequestMethod.GET, RequestMethod.POST})
    public String settings() {
        return "camera/settings";
    }

    // 카메라 조회
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Device> devices = cameraRepository.findAllByAssetIdOrderByNameAsc(assetId);
        List<CameraPolicy> policies = cameraPolicyRepository.findAll();
        HashMap<Long, CameraPolicy> m = new HashMap<>();
        policies.forEach(p -> m.put(p.getDeviceId(), p));
        devices.forEach(d -> {
            CameraPolicy p = m.get(d.getDeviceId());
            if (p != null) {
                d.setPolicy(p);
            }
        });
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

//
//
//    @RequestMapping(value = "logs/", method = {RequestMethod.GET, RequestMethod.POST})
//    public String displayLog(@ModelAttribute FactoryEventFilter filter, Pageable pageable, Model model) {
//        filter.tune(pageable);
//        model.addAttribute("filter", filter);
//        model.addAttribute("cameras", cameraRepository.findAllByAssetIdOrderByNameAsc(assetId));
//        model.addAttribute("assetId", assetId);
//        return "camera/log";
//    }

    // 정책 조회
    @GetMapping("{deviceId}/policy")
    public ResponseEntity<?> getPolicy(@PathVariable("deviceId") Long deviceId) {
        CameraPolicy policy = cameraPolicyRepository.findByDeviceId(deviceId);
        return new ResponseEntity<>(policy, HttpStatus.OK);
    }

    // 정책 업데이트
    @PatchMapping("{policyId}/policy")
    public ResponseEntity<?> savePolicy(@ModelAttribute CameraPolicy policy, @PathVariable("policyId") Long policyId) {

        Device device = cameraRepository.findById(policy.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("device", "deviceId", policy.getDeviceId()));

        policy.setPolicyId(policyId);
        policy.setAssetId(device.getAssetId());

        cameraPolicyRepository.save(policy);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * VXG API
     */

    // 카메라 정보 동기화
    @GetMapping("sync")
    public ResponseEntity<?> sync() {
        vxgService.sync(assetId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    // 스냅샷 조회
    @GetMapping("{deviceId}/snapshot")
    public ResponseEntity<?> getSnapshot(@PathVariable("deviceId") Long deviceId) {
        VxgSnapshot snapshot = vxgService.getSnapshot(deviceId);
        return new ResponseEntity<>(snapshot, HttpStatus.OK);
    }

    // 최근 이미지 조회
    @GetMapping("{deviceId}/images")
    public ResponseEntity<?> getImages(@PathVariable("deviceId") Long deviceId, Pageable pageable) {
        VxgImageResult result = vxgService.getImages(deviceId, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 스트리밍 URL 조회
    @GetMapping("{deviceId}/live/urls")
    public ResponseEntity<?> getWatchUrls(@PathVariable("deviceId") Long deviceId) {
        VxgWatchUrls result = vxgService.getWatchUrls(deviceId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
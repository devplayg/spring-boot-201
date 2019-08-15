package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Device;
import com.devplayg.coffee.repository.camera.CameraRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("cameras")
@Slf4j
public class CameraController {
    private static long assetId = 2;

    private final CameraRepository cameraRepository;

    private final VxgService vxgService;

    public CameraController(CameraRepository cameraRepository, VxgService vxgService) {
        this.cameraRepository = cameraRepository;
        this.vxgService = vxgService;
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String display(Model model) {
        model.addAttribute("cameras", cameraRepository.findAll());
        model.addAttribute("assetId", assetId);
        return "camera/camera";
    }

    @RequestMapping(value = "settings/", method = {RequestMethod.GET, RequestMethod.POST})
    public String settings() {
        return "camera/settings";
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Device> list = cameraRepository.findAllByAssetId(assetId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("sync")
    public ResponseEntity<?> sync() {
        vxgService.sync(assetId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("{id}/snapshot")
    public ResponseEntity<?> getSnapshot(@PathVariable("id") String id) {
        VxgSnapshot snapshot = vxgService.getSnapshot(assetId, id);
        return new ResponseEntity<>(snapshot, HttpStatus.OK);
    }

    @GetMapping("{id}/images")
    public ResponseEntity<?> getImages(@PathVariable("id") String id, Pageable pageable) {
        VxgImageResult result = vxgService.getImages(assetId, id, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("{id}/live/video")
    public ResponseEntity<?> getWatchUrls(@PathVariable("id") String id) {
        VxgWatchUrls result = vxgService.getWatchUrls(assetId, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

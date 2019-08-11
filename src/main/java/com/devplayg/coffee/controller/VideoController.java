package com.devplayg.coffee.controller;

import com.devplayg.coffee.service.VxgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("video")
@Slf4j
public class VideoController {

    private final VxgService vxgService;

    public VideoController(VxgService vxgService) {
        this.vxgService = vxgService;
    }

    @GetMapping("channels")
    public ResponseEntity<?> getChannels() {
        return new ResponseEntity<>(vxgService.getChannels(), HttpStatus.OK);
    }

    @GetMapping("channel/{id}")
    public ResponseEntity<?> getChannel(@PathVariable("id") long id) {
        return new ResponseEntity<>(vxgService.getChannel(id), HttpStatus.OK);
    }
}

package com.devplayg.coffee.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.TimeZone;

@RestController
@RequestMapping("public")
public class PublicController {

    @GetMapping("sysinfo")
    public HashMap<String, Object> getSystemInfo() {
        HashMap<String, Object> m = new HashMap<>();
        m.put("time", System.currentTimeMillis() / 1000);
        m.put("timezone", TimeZone.getDefault().toZoneId());
        return m;
    }
}

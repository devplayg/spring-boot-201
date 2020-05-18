package com.devplayg.coffee.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
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

    @GetMapping("settings")
    public HashMap<String, Object> getSettings(){
        HashMap<String, Object> m = new HashMap();
        m.put("name", "유니셈");
        m.put("workingTime", "8-17");

        ArrayList<String> members = new ArrayList<>(Arrays.asList("모차장님", "모과장님", "모대리님"));
        m.put("str_list", members);

        ArrayList<Integer> intArr = new ArrayList<>(Arrays.asList(101, 102, 103));
        m.put("int_list", intArr);

        ArrayList<Double> list = new ArrayList<>(Arrays.asList(100.00, 123.45, 555.45));
        m.put("float_list", list);

        ArrayList<Camera> cameras = new ArrayList<>(Arrays.asList(
                new Camera("Camera-101", "192.168.0.101", 20101),
                new Camera("Camera-101", "192.168.0.101", 20102),
                new Camera("Camera-101", "192.168.0.101", 20103)
        ));
        m.put("cameras", cameras);

        return m;
    }

    @AllArgsConstructor
    @Getter
    class Camera {
        private String name;
        private String ip;
        private int port;

    }
}

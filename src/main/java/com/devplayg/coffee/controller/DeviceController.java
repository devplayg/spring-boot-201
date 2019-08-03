package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.filter.DeviceFilter;
import com.devplayg.coffee.repository.device.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("device")
@Slf4j
public class DeviceController  {
    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    // Display
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String display(@ModelAttribute DeviceFilter filter, Pageable pageable, Model model) {
        filter.tune(pageable);
        model.addAttribute("filter", filter);
        return "device/device";
    }

    // Fetch
    @GetMapping
    public ResponseEntity<?> findAll(@ModelAttribute DeviceFilter filter) {
        if (filter.getCategory() > 0) {
            if (filter.getType() > 0) {
                return new ResponseEntity<>(deviceRepository.findByCategoryAndType(filter.getCategory(), filter.getType()), HttpStatus.OK);
            }
            return new ResponseEntity<>(deviceRepository.findByCategory(filter.getCategory()), HttpStatus.OK);
        }
        return new ResponseEntity<>(deviceRepository.findAll(), HttpStatus.OK);
    }
}

package com.devplayg.coffee.controller;

import com.devplayg.coffee.definition.PagingMode;
import com.devplayg.coffee.entity.filter.FactoryEventFilter;
import com.devplayg.coffee.entity.filter.NewsFilter;
import com.devplayg.coffee.framework.AssetManager;
import com.devplayg.coffee.service.FactoryEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("factoryevent")
@Slf4j
public class FactoryEventController {
    private final AssetManager assetManager;

    private final FactoryEventService factoryEventService;

    public FactoryEventController(FactoryEventService factoryEventService, AssetManager assetManager) {
        this.assetManager = assetManager;
        this.factoryEventService = factoryEventService;
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String display(@ModelAttribute FactoryEventFilter filter, Pageable pageable, Model model) {
        filter.tune(pageable);
        model.addAttribute("filter", filter);
        model.addAttribute("assetMap", assetManager.getAssetMap());

        if (filter.getViewMode() != null && filter.getViewMode().equals("realtime")) {
            return "factoryevent/realtime";
        }

        return "factoryevent/factoryevent";
    }

    @GetMapping
    public ResponseEntity<?> findAll(@ModelAttribute FactoryEventFilter filter, Pageable pageable) {
        filter.tune(pageable);
        if (filter.getPagingMode() == PagingMode.Paging.FastPaging.getValue()) {
            return new ResponseEntity<>(factoryEventService.getList(filter, pageable), HttpStatus.OK);
        }
        return new ResponseEntity<>(factoryEventService.getPageList(filter, pageable), HttpStatus.OK);
    }

    @GetMapping("{logId}/toggleEventType")
    public ResponseEntity<?> toggleEventType(@PathVariable long logId) {
        return new ResponseEntity<>(factoryEventService.toggleEventType(logId), HttpStatus.OK);
    }

    @GetMapping("news")
    public ResponseEntity<?> getNews(@ModelAttribute NewsFilter filter) {
        return new ResponseEntity<>(factoryEventService.getNews(filter), HttpStatus.OK);
    }
}


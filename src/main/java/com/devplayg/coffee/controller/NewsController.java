package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.filter.NewsFilter;
import com.devplayg.coffee.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("news")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public Object getNews(@ModelAttribute NewsFilter filter) {
        return newsService.get(filter);
    }
}

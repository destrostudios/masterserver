package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.database.NewsRepository;
import com.destrostudios.masterserver.database.schema.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @GetMapping
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @GetMapping("/latest")
    public List<News> getLatestNews(@RequestParam("limit") int limit) {
        return newsRepository.findNewest(limit);
    }
}

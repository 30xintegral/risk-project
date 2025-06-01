package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.NewsRequest;
import com.demo.riskproject.dto.response.NewsResponse;
import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@Slf4j
@CrossOrigin(origins = "https://intranet-banking.vercel.app", allowCredentials = "true")
public class NewsController {
    private final NewsService newsService;

    @PostMapping("/upload")
    public void uploadNews(@ModelAttribute NewsRequest newsRequest) {
        log.info("News upload started");
        newsService.addNews(newsRequest);
        log.info("News upload finished");
    }

    @GetMapping("/all")
    public PaginationResponse<NewsResponse> getAllNews(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("News fetch started");
        return newsService.getNews(page, size);
    }
}

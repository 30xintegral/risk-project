package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.NewsRequest;
import com.demo.riskproject.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@Slf4j
public class NewsController {
    private final NewsService newsService;

}

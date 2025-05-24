package com.demo.riskproject.service;

import com.demo.riskproject.dto.request.NewsRequest;
import com.demo.riskproject.dto.response.NewsResponse;
import com.demo.riskproject.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {
    void addNews(NewsRequest newsRequest);

    List<NewsResponse> getNews(int page, int size);
}

package com.demo.riskproject.service;

import com.demo.riskproject.dto.request.NewsRequest;
import com.demo.riskproject.dto.response.NewsResponse;
import com.demo.riskproject.dto.response.PaginationResponse;


public interface NewsService {
    void addNews(NewsRequest newsRequest);

    PaginationResponse<NewsResponse> getNews(int page, int size);
}

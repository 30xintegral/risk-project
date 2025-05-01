package com.demo.riskproject.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewsResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime publishDate;
}

package com.demo.riskproject.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewsRequest {
    private String title;
    private String content;
    private MultipartFile image;
    private LocalDateTime publishedDate;
}

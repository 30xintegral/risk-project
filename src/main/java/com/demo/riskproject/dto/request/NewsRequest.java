package com.demo.riskproject.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {
    private String title;
    private String content;
    private MultipartFile image;
    private LocalDateTime publishedDate;
}

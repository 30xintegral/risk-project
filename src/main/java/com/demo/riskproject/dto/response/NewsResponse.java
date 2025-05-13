package com.demo.riskproject.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime publishDate;
}

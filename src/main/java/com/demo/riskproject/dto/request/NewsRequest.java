package com.demo.riskproject.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {
    @NotNull
    private String title;
    @NotNull
    private String content;

    private MultipartFile image;
    @NotNull
    private LocalDateTime publishedDate;
}

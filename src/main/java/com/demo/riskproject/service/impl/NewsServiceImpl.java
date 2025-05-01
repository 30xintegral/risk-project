package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.request.NewsRequest;
import com.demo.riskproject.entity.News;
import com.demo.riskproject.exception.TerminatedException;
import com.demo.riskproject.mapper.NewsMapper;
import com.demo.riskproject.repository.NewsRepository;
import com.demo.riskproject.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final S3Client s3Client;
    private final NewsMapper newsMapper;

    @Value("${aws_s3_bucket_name}")
    private String bucketName;
    private String s3NewsImagesUrl = "https://30xinte-test.s3.eu-north-1.amazonaws.com/news-images/";

    @Override
    public void addNews(NewsRequest newsRequest) {
        try{
            News news = newsMapper.toEntity(newsRequest);
            news.setPublishDate(LocalDateTime.now());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(newsRequest.getImage().getOriginalFilename())
                    .contentType(newsRequest.getImage().getContentType())
                    .build();
            InputStream inputStream = new BufferedInputStream(newsRequest.getImage().getInputStream());
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, newsRequest.getImage().getSize()));
            log.info("news image uploaded to s3client ");
            news.setImageUrl(newsRequest.getImage().getOriginalFilename());
            newsRepository.save(news);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new TerminatedException(e + "\nNews upload failed");
        }
    }
}

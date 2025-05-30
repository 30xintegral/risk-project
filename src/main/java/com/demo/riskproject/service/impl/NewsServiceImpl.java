package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.request.NewsRequest;
import com.demo.riskproject.dto.response.NewsResponse;
import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.entity.News;
import com.demo.riskproject.exception.TerminatedException;
import com.demo.riskproject.mapper.NewsMapper;
import com.demo.riskproject.repository.NewsRepository;
import com.demo.riskproject.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final S3Client s3Client;
    private final NewsMapper newsMapper;

    @Value("${aws_s3_bucket_name}")
    private String bucketName;

    @Value("${aws_s3_bucket_news_url}")
    private String s3NewsImagesUrl;

    @Override
    public void addNews(NewsRequest newsRequest) {
        try{
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("news-images/" + newsRequest.getImage().getOriginalFilename())
                    .contentType(newsRequest.getImage().getContentType())
                    .build();
            InputStream inputStream = new BufferedInputStream(newsRequest.getImage().getInputStream());
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, newsRequest.getImage().getSize()));
            log.info("news image uploaded to s3client ");
            News news = News.builder()
                    .title(newsRequest.getTitle())
                    .content(newsRequest.getContent())
                    .publishDate(LocalDateTime.now())
                    .imageUrl(newsRequest.getImage().getOriginalFilename())
                    .build();
            newsRepository.save(news);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new TerminatedException(e + "\nNews upload failed");
        }
    }

    @Override
    public PaginationResponse<NewsResponse> getNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.findAll(pageable);
        List<News> newsList = newsPage.getContent();

        PaginationResponse<NewsResponse> newsPaginationResponse = new PaginationResponse<>();
        newsPaginationResponse.setPageSize(newsPage.getSize());
        newsPaginationResponse.setPageNumber(newsPage.getNumber());
        newsPaginationResponse.setTotalPages(newsPage.getTotalPages());
        newsPaginationResponse.setTotalElements(newsPage.getTotalElements());
        for (News news : newsList) {
            NewsResponse newsResponse = NewsResponse.builder().
                    id(news.getId()).
                    title(news.getTitle()).
                    content(news.getContent()).
                    publishDate(news.getPublishDate()).
                    imageUrl(s3NewsImagesUrl + news.getImageUrl()).
                    build();

            newsPaginationResponse.addData(newsResponse);
        }
        return newsPaginationResponse;
    }
}

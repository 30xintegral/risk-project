package com.demo.riskproject.mapper;

import com.demo.riskproject.dto.request.NewsRequest;
import com.demo.riskproject.dto.response.NewsResponse;
import com.demo.riskproject.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface NewsMapper {
    News toEntity(NewsRequest newsRequest);

    NewsResponse toResponse(News news);
}

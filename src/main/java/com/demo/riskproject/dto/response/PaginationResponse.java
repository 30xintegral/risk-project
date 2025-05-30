package com.demo.riskproject.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PaginationResponse<T> {
    int pageNumber;

    int pageSize;

    int totalPages;

    long totalElements;

    List<T> data = new ArrayList<>();

    public void addData(T data) {
        this.data.add(data);
    }
}

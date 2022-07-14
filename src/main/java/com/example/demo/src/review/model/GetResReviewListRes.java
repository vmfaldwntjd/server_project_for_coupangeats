package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetResReviewListRes {
    private double starAverage;
    private int userCount;
    private List<String> content;
}

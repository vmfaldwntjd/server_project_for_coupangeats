package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAllReviewRes {
    private String restaurantName;
    private String name;
    private int starPoint;
    private String url;
    private String menuName;
}

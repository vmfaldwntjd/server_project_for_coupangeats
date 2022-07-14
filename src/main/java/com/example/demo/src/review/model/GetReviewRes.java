package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {
    private int reviewId;
    private int userId;
    private String restaurantName;
    private int starPoint;
    private String content;
    private String menuName;
}

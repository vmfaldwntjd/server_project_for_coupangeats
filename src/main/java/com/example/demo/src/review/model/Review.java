package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@AllArgsConstructor
public class Review {
    private int reviewId;
    private int restaurantId;
    private int userId;
    private Integer starPoint;
    private String menuName;
    private String content;
}

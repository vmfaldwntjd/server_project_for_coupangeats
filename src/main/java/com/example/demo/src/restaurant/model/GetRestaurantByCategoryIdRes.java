package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetRestaurantByCategoryIdRes {
    private int restaurantId;
    private String resName;
    private List<String> resImageUrlList;
    private int isCheetah;
    private int deliveryTime;
    private double starPoint;
    private int reviewCount;
    private double distance;
    private int minDeliveryFee;
}

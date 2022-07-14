package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFNRestaurantRes {
    private int restaurantId;
    private String resName;
    private String resImageUrl;
    private int deliveryTime;
    private double starPoint;
    private int reviewCount;
    private double distance;
    private int minDeliveryFee;
}

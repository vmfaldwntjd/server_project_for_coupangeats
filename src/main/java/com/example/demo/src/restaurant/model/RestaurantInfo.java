package com.example.demo.src.restaurant.model;

import com.example.demo.src.cart.model.ResOrderMenuInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInfo {
    private int restaurantId;
    private String restaurantName;
    private boolean isCheetah;
    private boolean isPackable;
    private int deliveryTime;
    private int packagingTIme;
    private int deliveryFee;
    private int minOrderPrice; // 가게별 최소 주문 금액
    private List<ResOrderMenuInfo> resOrderMenuInfo;
}

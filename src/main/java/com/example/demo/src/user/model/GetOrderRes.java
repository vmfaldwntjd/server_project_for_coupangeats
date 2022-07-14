package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderRes {
    private String orderId;
    private int restaurantId;
    private int userId;
    private String restaurantName;
    private List<String> name;
    private String deliveryStatus;
    private int orderTotalPrice;
    private String url;
}

package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReceiptRes {
    private int receiptId;
    private String orderId;
    private String name;
    private int price;
    private String payInfo;
    private String restaurantName;
    private int discountFee;
    private String payBy;
    private int deliveryFee;
    private int orderPrice;
    private String deliveryAddress;
    private int orderTotalPrice;
    private int userId;
}

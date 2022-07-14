package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountInfo {
    private int couponCount;
    private int couponDiscount;
    private int cashDiscount;
    private int totalPrice;
}

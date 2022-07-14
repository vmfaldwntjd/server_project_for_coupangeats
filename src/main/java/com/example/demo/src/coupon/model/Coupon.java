package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coupon {
    private int couponId;
    private int status;
    private String couponNum;
    private int userId;
}

package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCouponRes {
    private int userId;
    private int couponId;
    private String couponImageUrl;
}

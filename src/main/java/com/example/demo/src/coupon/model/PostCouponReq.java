package com.example.demo.src.coupon.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCouponReq {
    private String couponNum;
}

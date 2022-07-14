package com.example.demo.src.cart.model;

import com.example.demo.src.restaurant.model.RecommendMenuInfo;
import com.example.demo.src.restaurant.model.RestaurantInfo;
import com.example.demo.src.user.model.GetUserAddressCartRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCartRes {
    private GetUserAddressCartRes getUserAddressCartRes; // core
    private RestaurantInfo restaurantInfo;
    private List<RecommendMenuInfo> recommendMenuInfoList;
    private DiscountInfo discountInfo;
    private RequestMessageInfo requestMessageInfo;
    //private PaymentInfo paymentInfo; // core
    private Object[] paymentInfo; // 임시 결제 정보.
}

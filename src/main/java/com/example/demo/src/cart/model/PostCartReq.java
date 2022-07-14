package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCartReq {
    private int userId;
    private int restaurantId;
    private int menuId;
    private int menuPrice;
    private int menuCount;
    private String menuName;
    private List<OptionKindInfo> optionKindInfoList;
}

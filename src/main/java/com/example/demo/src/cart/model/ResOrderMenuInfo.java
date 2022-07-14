package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResOrderMenuInfo {
    private int menuId;
    private int menuPrice;
    private int menuCount;
    private String menuName;
    private int menuOrder;
    private String optionInfo;
}

package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchCartMenuReq {
    private int menuId;
    private int menuOrder;
    private int menuCount;
}

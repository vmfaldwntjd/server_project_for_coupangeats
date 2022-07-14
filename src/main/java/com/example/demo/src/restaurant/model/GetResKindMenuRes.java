package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetResKindMenuRes {
    private int kindId;
    private String kindName;
    private int menuId;
    private String menuName;
    private int menuPrice;
    private String menuImageUrl;
    private String menuDescription;
}

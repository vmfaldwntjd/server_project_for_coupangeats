package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAddressDetail {
    private String detailAddress;
    private String wayGuide;
    private int kind;
    private String addressAlias;
}

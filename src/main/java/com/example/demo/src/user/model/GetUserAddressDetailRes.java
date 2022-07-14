package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserAddressDetailRes {
    private int userAddressId;
    private String detailAddress;
    private String doroNameAddress;
    private String wayGuide;
    private int kind;
    private String addressAlias;
    private String addressName;
}

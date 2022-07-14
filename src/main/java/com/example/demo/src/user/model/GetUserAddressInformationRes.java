package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserAddressInformationRes {
    private int userAddressId;
    private int kind;
    private String detailAddress;
    private String addressName;
    private String doroNameAddress;
}

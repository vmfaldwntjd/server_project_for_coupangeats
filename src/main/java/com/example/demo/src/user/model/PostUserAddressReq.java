package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserAddressReq {
    private String addressName;
    private String doroNameAddress;
    private String detailAddress;
    private String wayGuide;
    private String addressAlias;
    private int kind;
}

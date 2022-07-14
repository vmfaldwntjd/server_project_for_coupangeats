package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchUserAddressDetailReq {
    private String detailAddress;
    private String wayGuide;
    private int kind;
    private String addressAlias;
}

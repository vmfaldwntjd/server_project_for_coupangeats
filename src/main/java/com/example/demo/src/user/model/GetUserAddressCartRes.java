package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserAddressCartRes {
    public int userAddressId;
    public String userAddressName;
    public String doroNameAddress;
    public double latitude;
    public double longitude;
}

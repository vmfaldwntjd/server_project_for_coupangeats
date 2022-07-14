package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.security.DenyAll;

@Getter
@Setter
@AllArgsConstructor
public class PostUserPaymentReq {
    private String cardNum;
    private String validThruMonth;
    private String validThruYear;
    private String cvc;
    private String password;
    private String cardName;
}

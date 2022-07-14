package com.example.demo.src.payment.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUserAccountReq {
    private String accountNumber;
}

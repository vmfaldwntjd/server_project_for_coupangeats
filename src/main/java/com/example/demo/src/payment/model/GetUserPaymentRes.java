package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserPaymentRes {
    private int coopayId;
    private String balance;
    private int cardId;
    private String cardImageUrl;
    private String cardName;
    private String cardNum;
    private int accountId;
    private String accountImageUrl;
    private String accountName;
    private String accountNumber;
    private String cashReceiptStatus;
}

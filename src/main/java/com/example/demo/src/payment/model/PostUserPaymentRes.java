package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.SequenceGenerator;

@Getter
@Setter
@AllArgsConstructor
public class PostUserPaymentRes {
    private int cardId;
}

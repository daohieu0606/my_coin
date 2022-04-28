package com.example.mycoin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionModel {
    private String id;
    private long timeStamp;
    private String sender;
    private String receiver;
    private float value;
}

package com.example.mycoin.model;

import java.io.Serializable;

public class Wallet implements Serializable {
    public String privateKey;
    public String publicKey;
    public float balance;
}

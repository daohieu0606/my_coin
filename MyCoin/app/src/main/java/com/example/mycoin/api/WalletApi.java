package com.example.mycoin.api;

import com.example.mycoin.model.Wallet;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WalletApi {
    @POST("api/v1/wallets")
    Call<Wallet> createWallet();
}

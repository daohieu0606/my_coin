package com.example.mycoin.api;

import com.example.mycoin.model.BlockModel;
import com.example.mycoin.model.TransactionModel;
import com.example.mycoin.model.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WalletApi {
    @POST("api/v1/wallets")
    Call<Wallet> createWallet();

    @GET("api/v1/wallet")
    Call<Wallet> login(@Query("privateKey") String privateKey);

    @GET("api/v1/miner/get-latest-blocks")
    Call<BlockModel> getLatestBlocks();

    @GET("api/v1/wallets/history")
    Call<List<TransactionModel>> getHistoryTransactions();
}

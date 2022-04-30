package com.example.mycoin.api;

import com.example.mycoin.model.BlockModel;
import com.example.mycoin.model.TransactionModel;
import com.example.mycoin.model.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MinerApi {

    @GET("api/v1/miner/get-latest-blocks")
    Call<List<BlockModel>> getLatestBlocks();

    @GET("api/v1/miner/get-latest-transactions")
    Call<List<TransactionModel>> getLatestTransactions();
}

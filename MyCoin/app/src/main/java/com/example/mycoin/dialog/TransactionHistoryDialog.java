package com.example.mycoin.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoin.MainApplication;
import com.example.mycoin.R;
import com.example.mycoin.adapter.LatestBlockAdapter;
import com.example.mycoin.adapter.TransactionAdapter;
import com.example.mycoin.api.MinerApi;
import com.example.mycoin.api.RetrofitBuilder;
import com.example.mycoin.api.WalletApi;
import com.example.mycoin.model.BlockModel;
import com.example.mycoin.model.TransactionModel;
import com.example.mycoin.model.Wallet;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionHistoryDialog extends BaseDialog{

    private RecyclerView rcHistory;
    private TransactionAdapter transactionAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Transaction History");
        setContentResource(R.layout.dialog_transaction_history);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcHistory = view.findViewById(R.id.rcHistory);
        transactionAdapter = new TransactionAdapter();
        rcHistory.setAdapter(transactionAdapter);
        rcHistory.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();

        getHistoryTransactions();
    }

    private void getHistoryTransactions() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    MainApplication mainApplication = (MainApplication) getActivity().getApplication();

                    Retrofit retrofit = new RetrofitBuilder().getRetrofit();
                    WalletApi walletApi =
                            retrofit.create(WalletApi.class);
                    Call<List<TransactionModel>> call = walletApi.getHistoryTransactions(mainApplication.currentWallet.privateKey);

                    try {
                        Response<List<TransactionModel>> response = call.execute();
                        List<TransactionModel> transactionModels = response.body();

                        setNewListViewData(transactionModels);

                    } catch (IOException e ){
                    }
                } catch (Exception e) {
                }
            }
        });
        thread.start();
    }

    private void setNewListViewData(List<TransactionModel> transactionModels) {
        this.getActivity().runOnUiThread(()->{
            transactionAdapter.setAndNotifyNewDataList(transactionModels);
        });
    }
}

package com.example.mycoin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycoin.R;
import com.example.mycoin.adapter.LatestBlockAdapter;
import com.example.mycoin.api.MinerApi;
import com.example.mycoin.api.RetrofitBuilder;
import com.example.mycoin.api.WalletApi;
import com.example.mycoin.dialog.SendCoinDialog;
import com.example.mycoin.dialog.TransactionHistoryDialog;
import com.example.mycoin.model.BlockModel;
import com.example.mycoin.model.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private LatestBlockAdapter latestBlockAdapter;
    private RecyclerView rvLatestBlocks;
    private View btnLatestBlocks;
    private View btnLatestTransactions;
    private RelativeLayout btnSend;
    private RelativeLayout btnHistory;

    private ImageView imgLatestBlocks;
    private TextView txtLatestBlocks;
    private ImageView imgLatestTransactions;
    private TextView txtLatestTransactions;
    private TextView txtBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latestBlockAdapter = new LatestBlockAdapter();

        btnLatestBlocks = findViewById(R.id.btnLatestBlocks);
        btnLatestTransactions = findViewById(R.id.btnLatestTransactions);
        rvLatestBlocks = findViewById(R.id.rvLatestBlocks);
        imgLatestBlocks = findViewById(R.id.imgLatestBlocks);
        txtLatestBlocks = findViewById(R.id.txtLatestBlocks);
        imgLatestTransactions = findViewById(R.id.imgLatestTransactions);
        txtLatestTransactions = findViewById(R.id.txtLatestTransactions);
        btnSend = findViewById(R.id.btnSend);
        btnHistory = findViewById(R.id.btnHistory);
        txtBalance = findViewById(R.id.txtBalance);

        btnLatestBlocks.setOnClickListener((v)->{
            setCurrentTab(0);
        });
        btnLatestTransactions.setOnClickListener((v)->{
            setCurrentTab(1);
        });

        btnSend.setOnClickListener((v) ->{
            onSendButtonClicked();
        });

        btnHistory.setOnClickListener((v) ->{
            onBtnHistoryClick();
        });

        getLatestBlocks();

        rvLatestBlocks.setAdapter(latestBlockAdapter);

        rvLatestBlocks.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getLatestBlocks() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Retrofit retrofit = new RetrofitBuilder().getRetrofit();
                    MinerApi minerApi =
                            retrofit.create(MinerApi.class);
                    Call<List<BlockModel>> call = minerApi.getLatestBlocks("");

                    try {
                        Response<List<BlockModel>> response = call.execute();
                        List<BlockModel> blockModels = response.body();

                        setNewListViewData(blockModels);

                    } catch (IOException e ){
                    }
                } catch (Exception e) {
                }
            }
        });
        thread.start();
    }

    private void setNewListViewData(List<BlockModel> blockModels) {
        this.runOnUiThread(()->{
            latestBlockAdapter.setAndNotifyNewDataList(blockModels);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Wallet wallet = (Wallet) intent.getSerializableExtra("wallet");

        txtBalance.setText("$" + wallet.balance);
    }

    private void onBtnHistoryClick() {
        FragmentManager fm = getSupportFragmentManager();
        TransactionHistoryDialog transactionHistoryDialog = new TransactionHistoryDialog();
        transactionHistoryDialog.show(fm, transactionHistoryDialog.getTag());
    }

    private void onSendButtonClicked() {
        FragmentManager fm = getSupportFragmentManager();
        SendCoinDialog sendCoinDialog = new SendCoinDialog();
        sendCoinDialog.show(fm, sendCoinDialog.getTag());
    }

    private void setCurrentTab(int tabNum) {
        if(tabNum == 1) {
            setTextColor(txtLatestTransactions, R.color.mainColor);
            imgLatestTransactions.setColorFilter(ContextCompat.getColor(this, R.color.mainColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY);

            setTextColor(txtLatestBlocks, R.color.secondaryColor);
            imgLatestBlocks.setColorFilter(ContextCompat.getColor(this, R.color.secondaryColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        else {
            setTextColor(txtLatestTransactions, R.color.secondaryColor);
            imgLatestTransactions.setColorFilter(ContextCompat.getColor(this, R.color.secondaryColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY);

            setTextColor(txtLatestBlocks, R.color.mainColor);
            imgLatestBlocks.setColorFilter(ContextCompat.getColor(this, R.color.mainColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    public void setTextColor(TextView textView, int resource) {
        if(android.os.Build.VERSION.SDK_INT >= 23) {
            textView.setTextColor(ContextCompat.getColor(this, resource));
        } else {
            textView.setTextColor(getResources().getColor(resource));
        }
    }
}
package com.example.mycoin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycoin.MainApplication;
import com.example.mycoin.R;
import com.example.mycoin.adapter.LatestBlockAdapter;
import com.example.mycoin.adapter.TransactionAdapter;
import com.example.mycoin.api.MinerApi;
import com.example.mycoin.api.RetrofitBuilder;
import com.example.mycoin.api.WalletApi;
import com.example.mycoin.dialog.SendCoinDialog;
import com.example.mycoin.dialog.TransactionHistoryDialog;
import com.example.mycoin.model.BlockModel;
import com.example.mycoin.model.TransactionModel;
import com.example.mycoin.model.Wallet;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private LatestBlockAdapter latestBlockAdapter;
    private TransactionAdapter transactionAdapter;
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
    private int currentTab;

    private MainApplication mainApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mainApplication = (MainApplication) getApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latestBlockAdapter = new LatestBlockAdapter();
        transactionAdapter = new TransactionAdapter();

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
                    Call<List<BlockModel>> call = minerApi.getLatestBlocks();

                    try {
                        Response<List<BlockModel>> response = call.execute();
                        List<BlockModel> blockModels = response.body();

                        setNewLatestBlocks(blockModels);

                    } catch (IOException e ){
                    }
                } catch (Exception e) {
                }
            }
        });
        thread.start();
    }

    private void setNewLatestBlocks(List<BlockModel> blockModels) {
        this.runOnUiThread(()->{
            rvLatestBlocks.setAdapter(latestBlockAdapter);
            latestBlockAdapter.setAndNotifyNewDataList(blockModels);
        });
    }

    private void setNewTransactionBlocks(List<TransactionModel> transactionModels) {
        this.runOnUiThread(()->{
            rvLatestBlocks.setAdapter(transactionAdapter);
            transactionAdapter.setAndNotifyNewDataList(transactionModels);
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
        SendCoinDialog sendCoinDialog = new SendCoinDialog(() -> {
            updateWalletBalance();
            setCurrentTab(currentTab);
        });
        sendCoinDialog.show(fm, sendCoinDialog.getTag());
    }

    private void updateWalletBalance() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Retrofit retrofit = new RetrofitBuilder().getRetrofit();

                    WalletApi walletApi =
                            retrofit.create(WalletApi.class);
                    Call<Wallet> call = walletApi.login(mainApplication.currentWallet.privateKey);

                    try {
                        Response<Wallet> response = call.execute();
                        Wallet wallet = response.body();

                        if(wallet != null) {
                            mainApplication.currentWallet = wallet;
                            upDateWalletBalanceInUI(mainApplication.currentWallet.balance);
                        } else {
                            finish();
                        }

                    } catch (IOException e ){
                        finish();
                    }
                } catch (Exception e) {
                    finish();
                }
            }
        });

        thread.start();
    }

    private void upDateWalletBalanceInUI(float balance) {
        runOnUiThread(() -> {
            txtBalance.setText(String.valueOf(balance));
        });
    }

    private void setCurrentTab(int tabNum) {
        if(tabNum == 1) {
            setTextColor(txtLatestTransactions, R.color.mainColor);
            imgLatestTransactions.setColorFilter(ContextCompat.getColor(this, R.color.mainColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            btnLatestTransactions.setBackgroundColor(ContextCompat.getColor(this, R.color.whiteBg));

            setTextColor(txtLatestBlocks, R.color.secondaryColor);
            imgLatestBlocks.setColorFilter(ContextCompat.getColor(this, R.color.secondaryColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            btnLatestBlocks.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

            getLatestTransactions();
        }
        else {
            setTextColor(txtLatestTransactions, R.color.secondaryColor);
            imgLatestTransactions.setColorFilter(ContextCompat.getColor(this, R.color.secondaryColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            btnLatestBlocks.setBackgroundColor(ContextCompat.getColor(this, R.color.whiteBg));

            setTextColor(txtLatestBlocks, R.color.mainColor);
            imgLatestBlocks.setColorFilter(ContextCompat.getColor(this, R.color.mainColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            btnLatestTransactions.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

            getLatestBlocks();
        }
        currentTab = tabNum;
    }

    private void getLatestTransactions() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Retrofit retrofit = new RetrofitBuilder().getRetrofit();
                    MinerApi minerApi =
                            retrofit.create(MinerApi.class);
                    Call<List<TransactionModel>> call = minerApi.getLatestTransactions();

                    try {
                        Response<List<TransactionModel>> response = call.execute();
                        List<TransactionModel> transactionModels = response.body();

                        setNewTransactionBlocks(transactionModels);

                    } catch (IOException e ){
                    }
                } catch (Exception e) {
                }
            }
        });
        thread.start();
    }

    public void setTextColor(TextView textView, int resource) {
        if(android.os.Build.VERSION.SDK_INT >= 23) {
            textView.setTextColor(ContextCompat.getColor(this, resource));
        } else {
            textView.setTextColor(getResources().getColor(resource));
        }
    }
}
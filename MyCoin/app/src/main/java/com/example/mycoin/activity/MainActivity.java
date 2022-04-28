package com.example.mycoin.activity;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.mycoin.dialog.SendCoinDialog;
import com.example.mycoin.dialog.TransactionHistoryDialog;
import com.example.mycoin.model.BlockModel;
import com.example.mycoin.model.Wallet;

import java.util.ArrayList;
import java.util.List;

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
    private TextView txtWalletAddress;
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
        txtWalletAddress = findViewById(R.id.txtWalletAddress);
        txtBalance = findViewById(R.id.txtBalance);

        btnLatestBlocks.setOnClickListener((v)->{
            setCurrentTab(0);
        });
        btnLatestBlocks.setOnClickListener((v)->{
            setCurrentTab(1);
        });

        btnSend.setOnClickListener((v) ->{
            onSendButtonClicked();
        });

        btnHistory.setOnClickListener((v) ->{
            onBtnHistoryClick();
        });

        List<BlockModel> blockModels = new ArrayList<>();
        blockModels.add(new BlockModel());
        blockModels.add(new BlockModel());
        blockModels.add(new BlockModel());
        blockModels.add(new BlockModel());
        blockModels.add(new BlockModel());
        blockModels.add(new BlockModel());
        blockModels.add(new BlockModel());
        blockModels.add(new BlockModel());

        latestBlockAdapter.setAndNotifyNewDataList(blockModels);

        rvLatestBlocks.setAdapter(latestBlockAdapter);

        rvLatestBlocks.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Wallet wallet = (Wallet) intent.getSerializableExtra("wallet");

        txtWalletAddress.setText(wallet.privateKey);
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
//            txtLatestTransactions.setTextColor();
        }
        else {

        }
    }
}
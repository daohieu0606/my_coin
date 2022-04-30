package com.example.mycoin;

import android.app.Application;

import com.example.mycoin.model.Wallet;

public class MainApplication extends Application {
    public Wallet currentWallet;

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize Rudder SDK here
    }
}
package com.example.mycoin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycoin.R;
import com.example.mycoin.api.RetrofitBuilder;
import com.example.mycoin.api.WalletApi;
import com.example.mycoin.model.Wallet;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateWalletActivity extends AppCompatActivity {
    private Button btnGenerateWallet;
    private Button btnLogin;
    private TextView txtPrivateKeyValue;
    private TextView txtPublicKeyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        btnGenerateWallet = findViewById(R.id.btnGenerateWallet);
        btnLogin = findViewById(R.id.btnLogin);
        txtPrivateKeyValue = findViewById(R.id.txtPrivateKeyValue);
        txtPublicKeyValue = findViewById(R.id.txtPublicKeyValue);

        btnGenerateWallet.setOnClickListener((l) ->{
            onBtnGenerateWalletClicked();});
        btnLogin.setOnClickListener((l) ->{
            onBtnLoginClicked();});
    }

    private void onBtnLoginClicked() {
        finish();
    }

    private void onBtnGenerateWalletClicked() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Retrofit retrofit = new RetrofitBuilder().getRetrofit();

                    WalletApi walletApi =
                            retrofit.create(WalletApi.class);
                    Call<Wallet> call = walletApi.createWallet();

                    try {
                        Response<Wallet> response = call.execute();
                        Wallet wallet = response.body();

                        if(wallet != null) {
                            showCreateWalletSuccessful();
                            setWalletToView(wallet);
                        } else {
                            showCreateWalletError("create wallet failed. please try again.");
                        }

                    } catch (IOException e ){
                        showCreateWalletError(e.getMessage());
                    }
                } catch (Exception e) {
                    showCreateWalletError(e.getMessage());
                }
            }
        });

        thread.start();
    }

    private void showCreateWalletSuccessful() {
        runOnUiThread(()->{
            Toast.makeText(this, "successful: You have created a wallet", Toast.LENGTH_LONG).show();}
        );
    }

    private void setWalletToView(Wallet wallet) {
        runOnUiThread(() -> {
            txtPrivateKeyValue.setText(wallet.privateKey);
            txtPublicKeyValue.setText(wallet.publicKey);
        });
    }

    private void showCreateWalletError(String message) {
        this.runOnUiThread(()->{
            Toast.makeText(this, "error: " + message, Toast.LENGTH_LONG).show();});

    }
}
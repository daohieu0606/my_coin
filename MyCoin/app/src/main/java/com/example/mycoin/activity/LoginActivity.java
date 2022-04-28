package com.example.mycoin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mycoin.R;
import com.example.mycoin.api.RetrofitBuilder;
import com.example.mycoin.api.WalletApi;
import com.example.mycoin.model.Wallet;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private Button btnCreateWallet;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnCreateWallet = findViewById(R.id.btnCreateWallet);
        btnLogin = findViewById(R.id.btnLogin);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnCreateWallet.setOnClickListener((a) ->{
            Intent intent = new Intent(this, CreateWalletActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener((a) -> {
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

                            goToMainActivity(wallet);

                        } catch (IOException e ){
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();


        });
    }

    private void goToMainActivity(Wallet wallet) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("wallet", wallet);

        startActivity(intent);
    }
}
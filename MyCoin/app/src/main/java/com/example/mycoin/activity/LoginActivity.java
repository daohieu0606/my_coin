package com.example.mycoin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycoin.MainApplication;
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
    private EditText edtPrivateKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnCreateWallet = findViewById(R.id.btnCreateWallet);
        btnLogin = findViewById(R.id.btnLogin);
        edtPrivateKey = findViewById(R.id.edtPrivateKey);
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
                        Call<Wallet> call = walletApi.login(edtPrivateKey.getText().toString());

                        try {
                            Response<Wallet> response = call.execute();
                            Wallet wallet = response.body();

                            if(wallet != null) {
                                loginSuccess(wallet);
                            } else {
                                showLoginError("private key is incorrect");
                            }

                        } catch (IOException e ){
                            showLoginError(e.getMessage());
                        }
                    } catch (Exception e) {
                        showLoginError(e.getMessage());
                    }
                }
            });

            thread.start();


        });
    }

    private void showLoginError(String message) {
        this.runOnUiThread(()->{Toast.makeText(this, "error: " + message, Toast.LENGTH_LONG).show();});
    }

    private void loginSuccess(Wallet wallet) {
        MainApplication mainApplication = (MainApplication)getApplication();
        mainApplication.currentWallet = wallet;

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("wallet", wallet);
        startActivity(intent);
    }
}
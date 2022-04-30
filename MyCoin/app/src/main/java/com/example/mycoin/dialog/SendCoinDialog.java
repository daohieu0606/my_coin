package com.example.mycoin.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mycoin.MainApplication;
import com.example.mycoin.R;
import com.example.mycoin.api.RetrofitBuilder;
import com.example.mycoin.api.WalletApi;
import com.example.mycoin.model.TransactionModel;
import com.example.mycoin.model.Wallet;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SendCoinDialog extends BaseDialog {
    private EditText edtReceiverAddress;
    private EditText edtValue;
    private Button btnSend;

    private String privateKey;
    private OnDismissedListener onDismissedListener;

    public SendCoinDialog(OnDismissedListener onDismissedListener) {
        this.onDismissedListener = onDismissedListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Send Coin");
        setContentResource(R.layout.dialog_send_coin);

        MainApplication mainApplication = (MainApplication) getActivity().getApplication();
        privateKey = mainApplication.currentWallet.privateKey;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtReceiverAddress = view.findViewById(R.id.edtReceiverAddress);
        edtValue = view.findViewById(R.id.edtValue);
        btnSend = view.findViewById(R.id.btnSend);
    }

    @Override
    public void onStart() {
        super.onStart();

        btnSend.setOnClickListener((v) -> {
            processSendCoin();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(onDismissedListener != null)
            onDismissedListener.onDismissed();
    }

    private void processSendCoin() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Retrofit retrofit = new RetrofitBuilder().getRetrofit();

                    WalletApi walletApi =
                            retrofit.create(WalletApi.class);
                    Call<TransactionModel> call = walletApi.sendCoin(
                            privateKey,
                            edtReceiverAddress.getText().toString(),
                            Float.valueOf(edtValue.getText().toString()));

                    try {
                        Response<TransactionModel> response = call.execute();

                        if(response.code() == 200) {
                            TransactionModel transactionModel = response.body();
                            notifySendCoinSuccessful(transactionModel);
                        } else {
                            String message = "";
                            notifySendCoinFail(message);
                        }
                    } catch (IOException e ){
                        notifySendCoinFail(e.getMessage());
                    }
                } catch (Exception e) {
                    notifySendCoinFail(e.getMessage());
                }
            }
        });

        thread.start();
    }

    private void notifySendCoinFail(String message) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getActivity().getApplication(), "Have error", Toast.LENGTH_LONG).show();
        });
    }

    private void notifySendCoinSuccessful(TransactionModel transactionModel) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getActivity().getApplication(), "Success: you have sent coin successful", Toast.LENGTH_LONG).show();
        });
    }

    public interface OnDismissedListener {
        public void onDismissed();
    }
}

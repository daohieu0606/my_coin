package com.example.mycoin.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mycoin.R;

public abstract class BaseDialog extends DialogFragment {
    private int contentResource;
    private Button btnBack;
    private TextView txtTitle;

    protected String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_base, container, false);;

        if(contentResource != 0) {
            ViewStub stub = (ViewStub) view.findViewById(R.id.layout_stub);
            stub.setLayoutResource(contentResource);
            View inflated = stub.inflate();
        }

        btnBack = view.findViewById(R.id.btnBack);
        txtTitle = view.findViewById(R.id.txtTitle);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        txtTitle.setText(title);

        txtTitle.setOnClickListener((v) -> {dismiss();});
        btnBack.setOnClickListener((v) -> {dismiss();});
    }

    protected void setContentResource(int resource) {
        contentResource = resource;
    }
    protected void setTitle(String title) {
        this.title = title;
    }
}

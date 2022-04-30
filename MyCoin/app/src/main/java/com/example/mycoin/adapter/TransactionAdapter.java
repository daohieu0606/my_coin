package com.example.mycoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoin.R;
import com.example.mycoin.model.BlockModel;
import com.example.mycoin.model.TransactionModel;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<TransactionModel> transactionModels;

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_transaction, parent, false);

        TransactionAdapter.ViewHolder viewHolder = new TransactionAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        TransactionModel transactionModel = transactionModels.get(position);

        String createdTime = DurationFormatUtils.formatDuration(new Date().getTime() - transactionModel.timeStamp,
                "H:mm:ss ago", true);

        holder.txtTransactionId.setText(transactionModel.id);
        holder.txtTime.setText(createdTime);
        holder.txtSender.setText(transactionModel.sender);
        holder.txtReceiver.setText(transactionModel.receiver);
        holder.txtCoin.setText(String.valueOf(transactionModel.value) + " coins");
    }

    @Override
    public int getItemCount() {
        return transactionModels != null ? transactionModels.size() : 0;
    }

    public void setAndNotifyNewDataList(List<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTransactionId;
        private TextView txtTime;
        private TextView txtSender;
        private TextView txtReceiver;
        private TextView txtCoin;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTransactionId = itemView.findViewById(R.id.txtTransactionId);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtSender = itemView.findViewById(R.id.txtSender);
            txtReceiver = itemView.findViewById(R.id.txtReceiver);
            txtCoin = itemView.findViewById(R.id.txtCoin);
        }
    }
}

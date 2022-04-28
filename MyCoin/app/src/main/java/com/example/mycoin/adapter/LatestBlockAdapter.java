package com.example.mycoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoin.R;
import com.example.mycoin.model.BlockModel;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.Date;
import java.util.List;

public class LatestBlockAdapter extends RecyclerView.Adapter<LatestBlockAdapter.ViewHolder> {
    private List<BlockModel> blockModels;

    public void setAndNotifyNewDataList(List<BlockModel> blockModels) {
        this.blockModels = blockModels;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LatestBlockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_coin, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LatestBlockAdapter.ViewHolder holder, int position) {
        BlockModel blockModel = blockModels.get(position);

        String createdTime = DurationFormatUtils.formatDuration(new Date().getTime() - blockModel.timeStamp, "**H:mm:ss**", true);

        holder.txtBlockIndex.setText(String.valueOf(blockModel.index));
        holder.txtTime.setText(createdTime);
        holder.txtMinerName.setText(blockModel.miner);
        holder.txtTxnsCount.setText(String.valueOf(blockModel.transactionCount) + " txns");
        holder.txtCoin.setText(String.valueOf(blockModel.value) + " coins");
    }

    @Override
    public int getItemCount() {
        return blockModels != null ? blockModels.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtBlockIndex;
        private TextView txtTime;
        private TextView txtMinerName;
        private TextView txtTxnsCount;
        private TextView txtCoin;

        public ViewHolder(View itemView) {
            super(itemView);

            txtBlockIndex = itemView.findViewById(R.id.txtBlockIndex);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtMinerName = itemView.findViewById(R.id.txtMinerName);
            txtTxnsCount = itemView.findViewById(R.id.txtTxnsCount);
            txtCoin = itemView.findViewById(R.id.txtCoin);
        }
    }
}

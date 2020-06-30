package com.qiumi.app.wallet.wallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiumi.app.support.Constants;
import com.qiumi.app.support.component.FontTextView;
import com.qiumi.app.wallet.wallet.R;
import com.qiumi.app.wallet.wallet.api.output.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private Context context;
    private List<Record> recordList = new ArrayList<>();

    public RecordAdapter(Context mContext) {
        this.context = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_record, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Record record = recordList.get(position);
        if (null != record) {
            holder.tvType.setText(Constants.getOperName(record.opType) + " #" + record.transNo);
            holder.tvDate.setText(record.opTime);
            holder.tvAmount.setText(String.valueOf(record.amount));

            if (record.amount <= 0) {
                holder.tvAmount.setTextColor(context.getResources().getColor(R.color.black_33));
            } else {
                holder.tvAmount.setTextColor(context.getResources().getColor(R.color.yellow_d6));
            }
        }
    }

    public void clear() {
        if (null != recordList) {
            this.recordList.clear();
            notifyDataSetChanged();
        }
    }

    public void addAll(List<Record> recordList) {
        if (null != recordList) {
            this.recordList.clear();
            this.recordList.addAll(recordList);
            notifyDataSetChanged();
        }
    }

    public void add(List<Record> recordList) {
        if (null != recordList) {
            this.recordList.addAll(recordList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public Record getItem(int position) {
        if (null != recordList) {
            return recordList.get(position);
        }
        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private FontTextView tvType, tvDate, tvAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}

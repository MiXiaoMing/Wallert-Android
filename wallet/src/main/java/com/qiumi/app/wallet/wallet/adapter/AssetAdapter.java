package com.qiumi.app.wallet.wallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.qiumi.app.wallet.wallet.R;
import com.qiumi.app.wallet.wallet.api.output.Asset;

import java.util.ArrayList;
import java.util.List;

/**
 *  个人资产item
 */

public class AssetAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Asset> entities = new ArrayList<>();


    public AssetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_asset, null);
            viewHolder.ivItemIcon = convertView.findViewById(R.id.ivItemIcon);
            viewHolder.tvItemShortName = convertView.findViewById(R.id.tvItemShortName);
            viewHolder.tvItemName = convertView.findViewById(R.id.tvItemName);
            viewHolder.tvItemUSDT = convertView.findViewById(R.id.tvItemUSDT);
            viewHolder.tvItemRMB = convertView.findViewById(R.id.tvItemRMB);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Asset asset = entities.get(position);
        if (asset != null) {
            ImageLoader.circle(context, asset.icon, viewHolder.ivItemIcon);
            viewHolder.tvItemShortName.setText(asset.name);
            viewHolder.tvItemName.setText(asset.full_name);
            viewHolder.tvItemUSDT.setText(asset.price);
            viewHolder.tvItemRMB.setText("≈ ￥" + asset.priceTrans);
        }

        return convertView;
    }

    public void addAll(List<Asset> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView ivItemIcon;
        TextView tvItemShortName, tvItemName, tvItemUSDT, tvItemRMB;
    }
}

package com.qiumi.app.wallet.login;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.qiumi.app.support.api.output.CountryListResult;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * 国家adapter
 */

public class CityAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private ArrayList<CountryListResult> mData;
    private ArrayList<CountryListResult> mCopyData;
    private CityFilter mFilter;

    public CityAdapter(Context context, ArrayList<CountryListResult> data) {
        mContext = context;
        mData = data;
        mCopyData = new ArrayList<>(mData);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public CountryListResult getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.login_location_selection_item, parent, false);
            AutoUtils.auto(convertView);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvCity.setText(getItem(position).zh + " " + getItem(position).en);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new CityFilter();
        }
        return mFilter;
    }

    public ArrayList<CountryListResult> getFilterData() {
        return mData;
    }

    public static class ViewHolder {
        private TextView tvCity;


        public ViewHolder(View view) {
            tvCity = view.findViewById(R.id.tv_city);
        }
    }

    class CityFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<CountryListResult> newDatas = new ArrayList<>();
            if (TextUtils.isEmpty(constraint)) {
                newDatas.addAll(mCopyData);
            } else {
                if (mCopyData != null) {
                    for (CountryListResult mDatum : mCopyData) {
                        if (mDatum.zh.contains(constraint)) {
                            newDatas.add(mDatum);
                        }
                    }
                }
            }
            results.values = newDatas;
            results.count = newDatas.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData = (ArrayList<CountryListResult>) results.values;
            if (mData != null && mData.size() > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}

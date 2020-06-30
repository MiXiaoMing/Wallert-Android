package com.qiumi.app.wallet.mine.identification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qiumi.app.support.api.output.CountryListResult;
import com.qiumi.app.support.component.BaseViewInterface;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.identification.bean.MineCountryBean;

import java.util.ArrayList;

/**
 * 国家选择页列表adapter。
 */

public class MineSelectCountryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CountryListResult> mList;

    public MineSelectCountryAdapter(Context mContext) {
        this.mContext = mContext;
        // 数据管理要重视，否则容易出现引用问题，导致崩溃。
        mList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.mine_item_select_country, parent, false);
                holder = new Holder(convertView, mList.get(position));
                holder.initView(mContext);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.setDataToView();
            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 增加数据，可在上拉加载时调用。注意：要紧接着调用adapter.notifyDataSetChanged()。
     *
     * @param list list
     */
    public void addAll(ArrayList<CountryListResult> list) {
        if (mList != null && list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 清空数据，可在下拉刷新时调用。注意：要紧接着调用adapter.notifyDataSetChanged()。
     */
    void clear() {
        try {
            if (mList != null) {
                mList.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CountryListResult> getListData() {
        return mList;
    }

    private class Holder implements BaseViewInterface {
        TextView mTvCountryName;
        View mView;
        CountryListResult mBean;

        Holder(View mView, CountryListResult mBean) {
            this.mView = mView;
            this.mBean = mBean;
        }

        @Override
        public void initData() {

        }

        @Override
        public void initView(Context context) {
            mTvCountryName = mView.findViewById(R.id.tv_country_name);
        }

        @Override
        public void initListener() {

        }

        @Override
        public void setDataToView() {
            mTvCountryName.setText(mBean.zh + " " + mBean.en);
        }
    }
}

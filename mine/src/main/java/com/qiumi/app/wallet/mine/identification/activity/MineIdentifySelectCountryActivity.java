package com.qiumi.app.wallet.mine.identification.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.api.CustomArrayResult;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.api.output.CountryListResult;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;
import com.qiumi.app.wallet.mine.identification.activity.abroad.MineIdentifyAbroadIDCardVerifyActivity;
import com.qiumi.app.wallet.mine.identification.adapter.MineSelectCountryAdapter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择国家页。
 */

public class MineIdentifySelectCountryActivity extends AutoBaseActivity {
    private ListView mLvCountry;
    private MineSelectCountryAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_identification_select_country);

        initView();
        getCountryList();
    }

    private void initView() {
        findViewById(R.id.rlyBack).setOnClickListener(clickListener);

        mLvCountry = findViewById(R.id.lv_country);

        mAdapter = new MineSelectCountryAdapter(this);
        mLvCountry.setAdapter(mAdapter);
        mLvCountry.setOnItemClickListener(itemClickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.rlyBack) {
                finish();
            }
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            CountryListResult countryBean = mAdapter.getListData().get(position);

            // 跳转不同基本认证
            if (countryBean.zh.equals("中国")) {
                // 打开国内基本实名认证页。
                startActivity(new Intent(MineIdentifySelectCountryActivity.this, MineIdentifyIDCardVerifyActivity.class));
            } else {
                // 打开国外基本实名认证页。
                startActivity(new Intent(MineIdentifySelectCountryActivity.this, MineIdentifyAbroadIDCardVerifyActivity.class));
            }
        }
    };


    // 获取国家列表
    private void getCountryList() {
        new MineDataManager()
                .countryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomArrayResult<CountryListResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(CountryListResult result) {
                        if (result.data != null && result.data.size() > 0) {
                            mAdapter.addAll(result.data);
                        }
                    }
                });
    }
}

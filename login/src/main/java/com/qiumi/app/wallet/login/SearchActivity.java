package com.qiumi.app.wallet.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.data.set.CountryUtil;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.api.CustomArrayResult;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.wallet.login.api.LoginDataManager;
import com.qiumi.app.support.api.output.CountryListResult;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择手机号归属地
 */

public class SearchActivity extends AutoBaseActivity{

    public static final int SELECT_CITY_CODE = 0x0010;

    private EditText etSearch;
    private ListView lvCity;

    private CityAdapter mAdapter;
    private ArrayList<CountryListResult> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_location_selected_activity);

        initViews();

        mData = new ArrayList<>();
        for (int i = 0; i < CountryUtil.countryList.size(); ++i) {
            CountryListResult entity = new CountryListResult();
            entity.zh = CountryUtil.countryList.get(i);
            entity.en = CountryUtil.countryEnglishList.get(i);
            entity.code = CountryUtil.mobileAreaCode.get(i);

            mData.add(entity);
        }

        initData();

//        getCountryList();
    }

    private void initViews() {
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(clickListener);

        etSearch = findViewById(R.id.et_search);
        lvCity = findViewById(R.id.lv_city);
    }

    private void initData() {
        if (mData == null || mData.size() <= 0) {
            return;
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    updateKey(null);
                }else {
                    String key = s.toString();
                    updateKey(key);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter = new CityAdapter(this, mData);
        lvCity.setAdapter(mAdapter);
        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryListResult city = mAdapter.getFilterData().get(position);

                Logger.getLogger().d("选择了国家：" + city.zh);

                Intent intent = getIntent();
                intent.putExtra("name", city.code);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 更新搜索内容
     * @param key
     */
    private void updateKey(String key) {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(key);

            Logger.getLogger().e("size：" + mData.size());
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.iv_back) {
                finish();
            }
        }
    };

    // 获取国家列表
    private void getCountryList() {
        new LoginDataManager()
                .countryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomArrayResult<CountryListResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(CountryListResult result) {
                        mData = new ArrayList<>(result.data);
                        initData();
                    }
                });
    }
}

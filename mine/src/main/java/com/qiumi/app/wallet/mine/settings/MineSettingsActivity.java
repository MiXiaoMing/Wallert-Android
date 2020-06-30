package com.qiumi.app.wallet.mine.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.app.AppUtil;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.component.FontTextView;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.api.input.UpdateSettingBody;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;
import com.qiumi.app.wallet.mine.api.output.SettingInfoEntity;
import com.qiumi.app.wallet.mine.api.output.UpgradeEntity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 系统设置
 */

public class MineSettingsActivity extends AutoBaseActivity {

    private TextView tvVersion, tvRate, tvLanguage, tvType;

    private LinearLayout llyOptionLayout, llyOptions;

    private int type = 0; // 1:语言 2：货币

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_settings);
        initView();

        getInfo();
    }

    public void initView() {
        findViewById(R.id.rlyBack).setOnClickListener(clickListener);
        findViewById(R.id.tv_login_out).setOnClickListener(clickListener);

        tvLanguage = findViewById(R.id.tvLanguage);
        tvType = findViewById(R.id.tvType);
        tvRate = findViewById(R.id.tvRate);
        tvVersion = findViewById(R.id.tvVersion);

        tvVersion.setText(AppUtil.getVersionName(this));

        tvLanguage.setOnClickListener(clickListener);
        findViewById(R.id.ivLanguage).setOnClickListener(clickListener);

        tvType.setOnClickListener(clickListener);
        findViewById(R.id.ivType).setOnClickListener(clickListener);

        tvVersion.setOnClickListener(clickListener);
        findViewById(R.id.ivVersion).setOnClickListener(clickListener);


        // 选项
        llyOptionLayout = findViewById(R.id.llyOptionLayout);
        findViewById(R.id.viewCancel).setOnClickListener(clickListener);
        findViewById(R.id.tvCancel).setOnClickListener(clickListener);
        findViewById(R.id.tvSure).setOnClickListener(clickListener);
        llyOptions = findViewById(R.id.llyOptions);

        llyOptionLayout.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.rlyBack) {
                finish();
            } else if (id == R.id.tv_login_out) {
                loginOut();
            } else if (id == R.id.ivVersion || id == R.id.tvVersion) {
                getVersion();
            } else if (id == R.id.tvLanguage || id == R.id.ivLanguage) {
                AFToast.showShort(MineSettingsActivity.this, R.string.waiting);
//                if (llyOptionLayout.getVisibility() == View.VISIBLE) {
//                    llyOptionLayout.setVisibility(View.GONE);
//                } else {
//                    llyOptionLayout.setVisibility(View.VISIBLE);
//
//                    type = 1;
//                    addOperTypesOptions();
//                }
            } else if (id == R.id.tvType || id == R.id.ivType) {
                if (llyOptionLayout.getVisibility() == View.VISIBLE) {
                    llyOptionLayout.setVisibility(View.GONE);
                } else {
                    llyOptionLayout.setVisibility(View.VISIBLE);

                    type = 2;
                    addOperTypesOptions();
                }
            } else if (id == R.id.llyOptionLayout) {
                // 什么都不做，只是为了拦截点击事件
            } else if (id == R.id.viewCancel || id == R.id.tvCancel) {
                llyOptionLayout.setVisibility(View.GONE);
            } else if (id == R.id.tvSure) {
                llyOptionLayout.setVisibility(View.GONE);
            }
        }
    };

    private void addOperTypesOptions() {
        llyOptions.removeAllViews();

        List<Constants.IDNamePair> types = new ArrayList<>();
        if (type == 1) {
            types = Constants.languages;
        } else if (type == 2) {
            types = Constants.currency;
        }

        for (int i = 0; i < types.size(); i++) {
            addTextView(llyOptions, types.get(i).name, types.get(i).id);
        }
    }

    private void addTextView(final LinearLayout root, final String text, final String id) {
        final TextView textView = new FontTextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, AutoUtils.getPercentHeightSize(10));
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setTextSize(15);
        textView.setText(text);
        root.addView(textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llyOptionLayout.setVisibility(View.GONE);

                if (type == 1) {
                    // TODO: 2019/10/21 没有做具体处理
                    tvLanguage.setText(Constants.getLanguage(id));
//                    initLanguage(id);
                } else if (type == 2) {
                    tvType.setText(Constants.getCurrency(id));
                    com.qiumi.app.support.utils.AppUtil.setCurrencyType(id);
                }

                updateSetting();
            }
        });
    }

    /**
     * 退出登录。
     */
    private void loginOut() {
        UserUtil.clear();
        finish();
    }

    private void getVersion() {
        new MineDataManager()
                .upgrade()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<UpgradeEntity>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(UpgradeEntity result) {
                        if (result.getData().latestVerCode > AppUtil.getVersionCode(MineSettingsActivity.this)) {
                            tvVersion.setText(result.getData().latestver);
                            // TODO: 2019/10/21 升级弹框
                            // TODO: 2019/10/21 强制升级
                        }
                    }
                });
    }

    private void getInfo() {
        new MineDataManager()
                .settingInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<SettingInfoEntity>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(SettingInfoEntity result) {
                        tvLanguage.setText(Constants.getLanguage(result.getData().language));
                        tvType.setText(Constants.getCurrency(result.getData().currency));
                        tvRate.setText(result.getData().rate);
                    }
                });
    }

    private void updateSetting() {
        UpdateSettingBody body = new UpdateSettingBody();
        body.language = Integer.valueOf(Constants.getLanguageID(tvLanguage.getText().toString().trim()));
        body.currency = Integer.valueOf(Constants.getCurrencyID(tvType.getText().toString().trim()));

        new MineDataManager()
                .updateSetting(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                    }
                });
    }

    private void initLanguage(String id) {
        //应用内配置语言
        Resources resources = getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。

        Locale locale = Locale.getDefault();
        if (id.equals("2")) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (id.equals("3")) {
            locale = Locale.ENGLISH;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
        } else {
            config.locale = locale;
        }

        resources.updateConfiguration(config, dm);

        ARouter.getInstance().build(ARouterPaths.MAIN_ACTIVITY).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).navigation();
    }
}

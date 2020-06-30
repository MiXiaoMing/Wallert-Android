package com.qiumi.app.wallet.mine.identification.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.aoglrithm.Random;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.api.input.SubmitMainlandBody;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 基本身份认证 确认页面
 */
public class MineIdentifyIDCardVerifyMoreActivity extends AutoBaseActivity {

    private EditText etMessageCode;
    private TextView tvGetMessageCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mine_activity_verify_smscode);

        initView();
    }

    private void initView() {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);

        TextView tvSure = findViewById(R.id.tvSure);

        tvGetMessageCode = findViewById(R.id.tv_get_message_code);
        etMessageCode = findViewById(R.id.et_message_code);

        rlyBack.setOnClickListener(clickListener);
        tvSure.setOnClickListener(clickListener);
        tvGetMessageCode.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.rlyBack) {
                finish();
            } else if (viewId == R.id.tvSure) {
                String smsCode = etMessageCode.getText().toString().trim();
                if (TextUtils.isEmpty(smsCode)) {
                    AFToast.showShort(MineIdentifyIDCardVerifyMoreActivity.this, R.string.smsCode_empty);
                    return;
                }

                submit(smsCode);
            } else if (viewId == R.id.tv_get_message_code) {
                AFToast.showShort(MineIdentifyIDCardVerifyMoreActivity.this, R.string.waiting);
//                getSmsCode();
            }
        }
    };

    private CountDownTimer mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long value = millisUntilFinished / 1000;
            if (value == 0) {
                tvGetMessageCode.setText(getResources().getString(R.string.get_message_code));
                tvGetMessageCode.setEnabled(true);
            } else {
                String second = value + "s";
                tvGetMessageCode.setText(second);
            }
        }

        @Override
        public void onFinish() {
            tvGetMessageCode.setText(getResources().getString(R.string.get_message_code));
            tvGetMessageCode.setEnabled(true);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    // TODO: 2020/3/6 需要修改访问地址
    // 获取手机验证码
    private void getSmsCode(String areaCode, String mobile) {
        SmsCodeBody body = new SmsCodeBody();
        body.mobile = mobile;
        body.countryCode = areaCode;

        Map<String, String> signParams = new HashMap<>();
        signParams.put("mobile", mobile);
        body.sig = SignUtil.generate(signParams, Constants.PREKEY);

//        new SupportDataManager()
//                .smsCode(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CustomResult<EmptyHttpResult>() {
//
//                    @Override
//                    public void onError() {
//
//                    }
//
//                    @Override
//                    public void onSuccess(EmptyHttpResult result) {
//                        tvGetMessageCode.setEnabled(false);
//                        if (mCountDownTimer != null) {
//                            mCountDownTimer.start();
//                        }
//                    }
//                });
    }

    private void submit(String smsCode) {
        SubmitMainlandBody body = new SubmitMainlandBody();

        body.fileCode = getIntent().getStringExtra("fileToken");
        body.smsCode = smsCode;
        body.timestamp = String.valueOf(System.currentTimeMillis());
        body.nonce = Random.getUUID();

        Map<String, String> signParams = new HashMap<>();
        signParams.put("fileCode", body.fileCode);
        signParams.put("smsCode", body.smsCode);
        signParams.put("timestamp", body.timestamp);
        signParams.put("nonce", body.nonce);
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new MineDataManager()
                .submitMainland(body)
                .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        AFToast.showShort(MineIdentifyIDCardVerifyMoreActivity.this, "认证成功");
                        finish();
                    }
                });
    }
}

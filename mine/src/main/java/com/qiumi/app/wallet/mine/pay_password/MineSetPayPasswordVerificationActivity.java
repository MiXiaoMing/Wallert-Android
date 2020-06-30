package com.qiumi.app.wallet.mine.pay_password;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.data.encoded.Hex;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.api.input.SetPayPassBody;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置支付密码获取验证码页。（第二步）
 */

public class MineSetPayPasswordVerificationActivity extends AutoBaseActivity {
    private String password = "";
    private EditText etPassword, etSmsCode;
    private TextView tvSmsCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_set_pay_password_verification);

        password = getIntent().getStringExtra("password");
        if (TextUtils.isEmpty(password)) {
            finish();
            return;
        }

        initView();
    }

    private void initView() {
        findViewById(R.id.rlyBack).setOnClickListener(clickListener);

        etPassword = findViewById(R.id.etPassword);
        etSmsCode = findViewById(R.id.etSmsCode);
        tvSmsCode = findViewById(R.id.tvSmsCode);

        TextView tvSubmit = findViewById(R.id.tvSubmit);

        tvSmsCode.setOnClickListener(clickListener);
        tvSubmit.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.rlyBack) {
                finish();
            } else if (id == R.id.tvSmsCode) {
                String telephone = UserUtil.getTelephoneFull();
                String areaCode = UserUtil.getCountryCode();
                if (TextUtils.isEmpty(telephone) || TextUtils.isEmpty(areaCode)) {
                    // TODO: 2020/3/6 需跳转登录
                    Logger.getLogger().e("本地读取手机号为空");
                    ARouter.getInstance().build(ARouterPaths.LOGIN_ACTIVITY).navigation();
                    finish();
                    return;
                }
                getSmsCode(telephone, areaCode);
            } else if (id == R.id.tvSubmit) {
                String smsCode = etSmsCode.getText().toString().trim();
                if (TextUtils.isEmpty(smsCode)) {
                    AFToast.showShort(MineSetPayPasswordVerificationActivity.this, R.string.smsCode_empty);
                    return;
                }
                submit(smsCode);
            }
        }
    };

    private CountDownTimer mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long value = millisUntilFinished / 1000;
            if (value == 0) {
                tvSmsCode.setText(getResources().getString(R.string.get_verify_code));
                tvSmsCode.setEnabled(true);
            } else {
                String second = value + "s";
                tvSmsCode.setText(second);
            }
        }

        @Override
        public void onFinish() {
            tvSmsCode.setText(getResources().getString(R.string.get_verify_code));
            tvSmsCode.setEnabled(true);
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

    // 获取手机验证码
    private void getSmsCode(String mobile, String areaCode) {
        SmsCodeBody body = new SmsCodeBody();
        body.mobile = mobile;
        body.countryCode = areaCode;

        Map<String, String> signParams = new HashMap<>();
        signParams.put("mobile", mobile);
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new MineDataManager()
                .smsCode_Pay(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        tvSmsCode.setEnabled(false);
                        if (mCountDownTimer != null) {
                            mCountDownTimer.start();
                        }
                    }
                });
    }

    /**
     * 提交修改密码
     */
    private void submit(String smsCode) {
        SetPayPassBody body = new SetPayPassBody();
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA256");
            sha.update((getNextSalt() + password).getBytes("UTF-8"));
            body.payPass = Hex.encode(sha.digest()).toUpperCase();
        } catch (Exception e) {
            Logger.getLogger().e("签名异常：\n" + password + "\n" + e.toString());
            return ;
        }
        body.smsCode = smsCode;
        body.timestamp = String.valueOf(System.currentTimeMillis());
        body.nonce = com.appframe.utils.aoglrithm.Random.getUUID();

        Map<String, String> signParams = new HashMap<>();
        signParams.put("payPass", body.payPass);
        signParams.put("smsCode", body.smsCode);
        signParams.put("timestamp", body.timestamp);
        signParams.put("nonce", body.nonce);
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new MineDataManager()
                .setPayPass(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        if (ActivityUtil.isAvailable(MineSetPayPasswordVerificationActivity.this)) {
                            AFToast.showShort(MineSetPayPasswordVerificationActivity.this, R.string.pay_code_success);
                            finish();
                        }
                    }
                });
    }

    public String getNextSalt() {
        try {
            Random random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            return new String(salt,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Logger.getLogger().e("生成salt错误：" + e.toString());
            return String.valueOf(System.currentTimeMillis());
        }
    }
}

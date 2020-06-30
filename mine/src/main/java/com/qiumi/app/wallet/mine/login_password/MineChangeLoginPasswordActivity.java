package com.qiumi.app.wallet.mine.login_password;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.qiumi.app.wallet.mine.api.input.UpdatePassBody;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改登录密码页。
 */

public class MineChangeLoginPasswordActivity extends AutoBaseActivity {

    private EditText etOrgPassword, etNewPassword, etConfirmNewPassword;
    private EditText etSmsCode;
    private TextView tvSmsCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_change_login_password);

        initView();
    }

    private void initView() {
        findViewById(R.id.rlyBack).setOnClickListener(clickListener);

        etOrgPassword = findViewById(R.id.etOrgPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);

        etSmsCode = findViewById(R.id.etSmsCode);
        tvSmsCode = findViewById(R.id.tvSmsCode);

        tvSmsCode.setOnClickListener(clickListener);
        findViewById(R.id.tvSubmit).setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.rlyBack) {
                finish();
            } else if (id == R.id.tvSubmit) {
                submit();
            } else if (id == R.id.tvSmsCode) {
                tvSmsCode.setEnabled(false);
                getSmsCode();
            }
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

    private CountDownTimer mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long value = millisUntilFinished / 1000;
            if (value == 0) {
                tvSmsCode.setText(getResources().getString(R.string.get_message_code));
                tvSmsCode.setEnabled(true);
            } else {
                String second = value + "s";
                tvSmsCode.setText(second);
            }
        }

        @Override
        public void onFinish() {
            tvSmsCode.setText(getResources().getString(R.string.get_message_code));
            tvSmsCode.setEnabled(true);
        }
    };

    // 获取手机验证码
    private void getSmsCode() {
        new MineDataManager()
                .smsCode_Login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {

                    @Override
                    public void onError() {
                        tvSmsCode.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        if (mCountDownTimer != null) {
                            mCountDownTimer.start();
                        }
                    }
                });
    }

    /**
     * 提交修改登录密码
     */
    private void submit() {
        String orgPassword = etOrgPassword.getText().toString().trim();
        if (TextUtils.isEmpty(orgPassword)) {
            AFToast.showShort(this, R.string.mine_change_login_password_old_login_password_hint);
            return;
        }

        String newPassword = etNewPassword.getText().toString().trim();
        if (TextUtils.isEmpty(newPassword)) {
            AFToast.showShort(this, R.string.mine_change_login_password_new_login_password_hint);
            return;
        }

        String confirmPassword = etConfirmNewPassword.getText().toString().trim();
        if (TextUtils.isEmpty(confirmPassword)) {
            AFToast.showShort(this, R.string.mine_change_login_password_confirm_login_password_hint);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            AFToast.showShort(this, R.string.mine_set_password_enter_password_not_same_hit);
            return;
        }

        String smsCode = etSmsCode.getText().toString().trim();
        if (TextUtils.isEmpty(smsCode)) {
            AFToast.showShort(this, R.string.smsCode_empty);
            return;
        }

        UpdatePassBody body = new UpdatePassBody();
        body.orgPassword = orgPassword;
        body.newPassword = newPassword;
        body.smsCode = smsCode;
        body.timestamp = String.valueOf(System.currentTimeMillis());
        body.nonce = Random.getUUID();

        Map<String, String> signParams = new HashMap<>();
        signParams.put("orgPassword", body.orgPassword);
        signParams.put("newPassword", body.newPassword);
        signParams.put("smsCode", body.smsCode);
        signParams.put("timestamp", body.timestamp);
        signParams.put("nonce", body.nonce);
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new MineDataManager()
                .updateLoginPassword(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        if (ActivityUtil.isAvailable(MineChangeLoginPasswordActivity.this)) {
                            AFToast.showShort(MineChangeLoginPasswordActivity.this, R.string.login_password_success);
                            finish();
                        }
                    }
                });
    }
}

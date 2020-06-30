package com.qiumi.app.wallet.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.library.component.notify.AFToast;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.wallet.login.api.input.RetrieveSmsCodeBody;
import com.qiumi.app.wallet.login.api.input.UpdatePassBody;
import com.qiumi.app.wallet.login.utils.SecurityVerityType;

/**
 * 找回密码
 */
public class RetrieveActivity extends AutoBaseActivity {

    private static final String TAG = "RegisterActivity";

    private EditText etTelephone, etPassword;
    private EditText etMessageCode;
    private TextView tvGetMessageCode;
    private TextView tvLocation;

    private SecurityVerityType mCurType = SecurityVerityType.CHINA_BASIC;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_retrieve_password);

        initView();
    }

    private void initView() {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);

        TextView tvSubmit = findViewById(R.id.tv_submit);

        rlyBack.setOnClickListener(clickListener);
        tvSubmit.setOnClickListener(clickListener);

        LinearLayout llLcation = findViewById(R.id.ll_location);
        llLcation.setOnClickListener(clickListener);
        tvLocation = findViewById(R.id.tv_location);

        tvGetMessageCode = findViewById(R.id.tv_get_message_code);
        tvGetMessageCode.setOnClickListener(clickListener);
        etMessageCode = findViewById(R.id.et_message_code);

        TextView tvGoToLogin = findViewById(R.id.tv_go_to_login);
        tvGoToLogin.setOnClickListener(clickListener);

        //设置edittext字体
        etTelephone = findViewById(R.id.etTelephone);
        etPassword = findViewById(R.id.etPassword);
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.rlyBack) {
                finish();
            } else if (viewId == R.id.tv_submit) {
                showSecurityVerity();
            } else if (viewId == R.id.ll_location) {
                showLocationOption();
            }  else if (viewId == R.id.tv_go_to_login) {
                ARouter.getInstance().build(ARouterPaths.LOGIN_ACTIVITY).navigation();
                finish();
            } else if (viewId == R.id.tv_get_message_code) {
                String mobile = etTelephone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    AFToast.showShort(RetrieveActivity.this, R.string.mobile_empty);
                    return;
                }

                // TODO: 2019/9/23 需要判断手机号合法性

                getSmsCode(mobile);

                tvGetMessageCode.setEnabled(false);
                if (mCountDownTimer != null) {
                    mCountDownTimer.start();
                }

            }
        }
    };

    /**
     * 安全校验
     */
    private void showSecurityVerity() {
        LoginSecurityVerityDialog dialog = LoginSecurityVerityDialog.getInstance(SecurityVerityType.ABROAD_BASIC);
        dialog.show(getFragmentManager(), "security_verity");
    }

    private CountDownTimer mCountDownTimer = new CountDownTimer(60 *1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long value = millisUntilFinished / 1000;
            if (value == 0) {
                tvGetMessageCode.setText(getResources().getString(R.string.get_message_code));
                tvGetMessageCode.setEnabled(true);
            }else {
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

    /**
     * 选择手机号归属地
     */
    private void showLocationOption() {
        ARouter.getInstance().build(ARouterPaths.LOGIN_SELECT_COUNTRY_ACTIVITY).navigation(this,SearchActivity.SELECT_CITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SearchActivity.SELECT_CITY_CODE) {
                String name = data.getStringExtra("name");
                if (!TextUtils.isEmpty(name)) {
                    tvLocation.setText(name);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    // 获取手机验证码
    private void getSmsCode(String mobile) {
        RetrieveSmsCodeBody body = new RetrieveSmsCodeBody();
        body.mobile = mobile;

//        new LoginDataManager()
//                .retrieveSmsCode(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<EmptyHttpResult>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        Logger.getLogger().e("获取手机验证码：" + e.getMessage());
//                        if (RetrieveActivity.this.isDestroyed() && !RetrieveActivity.this.isFinishing())
//                            AFToast.showShort(RetrieveActivity.this, "网络错误，请稍后再试");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(EmptyHttpResult result) {
//                        if (!result.getStatus().equals("0")) {
//                            Logger.getLogger().e("获取手机验证码，msgCode：" + result.getMessage() + "/n");
//                            if (RetrieveActivity.this.isDestroyed() && !RetrieveActivity.this.isFinishing()) {
//                                if (TextUtils.isEmpty(result.getMessage()))
//                                    AFToast.showShort(RetrieveActivity.this, result.getMessage());
//                                else
//                                    AFToast.showShort(RetrieveActivity.this, "网络错误，请稍后再试");
//                            }
//                        }
//                    }
//                });
    }

    // 修改密码
    private void updatePassword(String mobile, String smsCode, String password) {
        UpdatePassBody body = new UpdatePassBody();
        body.code = smsCode;
        body.new_password = password;

//        new LoginDataManager()
//                .updatePass(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<EmptyHttpResult>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        Logger.getLogger().e("修改密码：" + e.getMessage());
//                        if (RetrieveActivity.this.isDestroyed() && !RetrieveActivity.this.isFinishing())
//                            AFToast.showShort(RetrieveActivity.this, "网络错误，请稍后再试");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(EmptyHttpResult result) {
//                        if (!result.getStatus().equals("0")) {
//                            Logger.getLogger().e("修改密码，msgCode：" + result.getMessage() + "/n");
//                            if (RetrieveActivity.this.isDestroyed() && !RetrieveActivity.this.isFinishing()) {
//                                if (TextUtils.isEmpty(result.getMessage()))
//                                    AFToast.showShort(RetrieveActivity.this, result.getMessage());
//                                else
//                                    AFToast.showShort(RetrieveActivity.this, "网络错误，请稍后再试");
//                            }
//                        }
//                    }
//                });
    }
}

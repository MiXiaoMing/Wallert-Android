package com.qiumi.app.wallet.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.aoglrithm.Random;
import com.appframe.utils.string.VerifyPhoneNumber;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.support.utils.LanguageUtil;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.login.api.LoginDataManager;
import com.qiumi.app.wallet.login.api.input.SignUpBody;
import com.qiumi.app.wallet.login.api.output.SignUpResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 注册页面
 */
public class RegisterActivity extends AutoBaseActivity {

    private static final String TAG = "RegisterActivity";

    private EditText etTelephone, etPassword;
    private ImageView ivPasswordOption;
    private TextView tvLocation;
    private EditText etMessageCode;
    private TextView tvGetMessageCode;
    private EditText etInviteID;
    private ImageView ivReadService;
    private TextView tvService, tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);

        tvRegister = findViewById(R.id.tv_register);

        rlyBack.setOnClickListener(clickListener);
        tvRegister.setOnClickListener(clickListener);

        LinearLayout llLcation = findViewById(R.id.ll_location);
        tvLocation = findViewById(R.id.tv_location);
        ivPasswordOption = findViewById(R.id.iv_password_option);

        llLcation.setOnClickListener(clickListener);
        ivPasswordOption.setOnClickListener(clickListener);

        tvGetMessageCode = findViewById(R.id.tv_get_message_code);
        tvGetMessageCode.setOnClickListener(clickListener);
        etMessageCode = findViewById(R.id.et_message_code);
        etInviteID = findViewById(R.id.et_invite_id);
        ivReadService = findViewById(R.id.iv_read_service);
        tvService = findViewById(R.id.tv_service);

        ivReadService.setOnClickListener(clickListener);
        setServiceFormat();


        TextView tvGoToLogin = findViewById(R.id.tv_go_to_login);
        tvGoToLogin.setOnClickListener(clickListener);

        //设置edittext字体
        etTelephone = findViewById(R.id.etTelephone);
        etPassword = findViewById(R.id.etPassword);
    }

    /**
     * 设置 服务条款样式
     * 我已阅读服务条款和隐私条款
     */
    private void setServiceFormat() {
        if (LanguageUtil.isCNLanguage()) {
            String content = getResources().getString(R.string.service);
            SpannableStringBuilder ssb = new SpannableStringBuilder(content);
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    widget.setBackgroundColor(0x00000000);
                    AFToast.showShort(RegisterActivity.this, "服务条款");
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.blue));
                    ds.setUnderlineText(false);
                }
            }, 4, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    widget.setBackgroundColor(0x00000000);
                    AFToast.showShort(RegisterActivity.this, "隐私条款");
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.blue));
                    ds.setUnderlineText(false);
                }
            }, 9, 13, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            tvService.setMovementMethod(LinkMovementMethod.getInstance());
            tvService.setText(ssb, TextView.BufferType.SPANNABLE);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.rlyBack) {
                finish();
            } else if (viewId == R.id.tv_register) {
                String mobile = etTelephone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    AFToast.showShort(RegisterActivity.this, R.string.mobile_empty);
                    return;
                }

                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    AFToast.showShort(RegisterActivity.this, R.string.mobile_empty);
                    return;
                }

                if (password.length() < 6) {
                    AFToast.showShort(RegisterActivity.this, R.string.password_size_less);
                    return;
                }

                if (password.length() > 20) {
                    AFToast.showShort(RegisterActivity.this, R.string.password_size_many);
                    return;
                }

                String smsCode = etMessageCode.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    AFToast.showShort(RegisterActivity.this, R.string.smsCode_empty);
                    return;
                }

                String areaCode = tvLocation.getText().toString().trim();
                if (TextUtils.isEmpty(areaCode) || areaCode.length() == 1) {
                    AFToast.showShort(RegisterActivity.this, R.string.mobile_area_code_empty);
                    return;
                }

                String inviteID = etInviteID.getText().toString().trim();

                signUp(areaCode, mobile, password, smsCode, inviteID);
            } else if (viewId == R.id.ll_location) {
                startActivityForResult(new Intent(RegisterActivity.this, SearchActivity.class), SearchActivity.SELECT_CITY_CODE);
            } else if (viewId == R.id.iv_password_option) {
                if (ivPasswordOption.isSelected()) {
                    /**
                     * 密码不可见
                     */
                    ivPasswordOption.setSelected(false);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    if (!TextUtils.isEmpty(etPassword.getText().toString())) {
                        etPassword.setSelection(etPassword.getText().toString().length());
                    }
                } else {
                    /**
                     * 密码可见
                     */
                    ivPasswordOption.setSelected(true);
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    if (!TextUtils.isEmpty(etPassword.getText().toString())) {
                        etPassword.setSelection(etPassword.getText().toString().length());
                    }
                }
            } else if (viewId == R.id.tv_go_to_login) {
                finish();
            } else if (viewId == R.id.tv_get_message_code) {
                String areaCode = tvLocation.getText().toString().trim();
                if (TextUtils.isEmpty(areaCode)) {
                    AFToast.showShort(RegisterActivity.this, R.string.mobile_area_code_empty);
                    return;
                }

                String mobile = etTelephone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    AFToast.showShort(RegisterActivity.this, R.string.mobile_empty);
                    return;
                } else if (!VerifyPhoneNumber.isAvailable(areaCode, mobile)) {
                    AFToast.showShort(RegisterActivity.this, R.string.mobile_not_available);
                    return;
                }

                getSmsCode(areaCode, mobile);
            } else if (viewId == R.id.iv_read_service) {
                if (ivReadService.isSelected()) {
                    ivReadService.setSelected(false);
                    tvRegister.setBackgroundColor(getResources().getColor(R.color.gray_9f));
                    tvRegister.setClickable(false);
                } else {
                    ivReadService.setSelected(true);
                    tvRegister.setBackgroundColor(getResources().getColor(R.color.blue));
                    tvRegister.setClickable(true);
                }
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
    private void getSmsCode(String areaCode, String mobile) {
        SmsCodeBody body = new SmsCodeBody();
        body.mobile = mobile;
        body.countryCode = areaCode;

        Map<String, String> signParams = new HashMap<>();
        signParams.put("mobile", mobile);
        body.sig = SignUtil.generate(signParams, Constants.PREKEY);

        new LoginDataManager()
                .smsCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        tvGetMessageCode.setEnabled(false);
                        if (mCountDownTimer != null) {
                            mCountDownTimer.start();
                        }
                    }
                });
    }

    // 注册
    private void signUp(String areaCode, String mobile, String password, String smsCode, String inviteID) {
        SignUpBody body = new SignUpBody();
        body.countryCode = areaCode;
        body.mobile = mobile;
        body.password = password;
        body.sMSCode = smsCode;
        body.invitor = inviteID;
        body.timestamp = String.valueOf(System.currentTimeMillis());
        body.nonce = Random.getUUID();

        Map<String, String> signParams = new HashMap<>();
        signParams.put("mobile", mobile);
        signParams.put("password", password);
        signParams.put("timestamp", body.timestamp);
        signParams.put("nonce", body.nonce);
        body.sig = SignUtil.generate(signParams, Constants.PREKEY);

        new LoginDataManager()
                .signUp(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<SignUpResult>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(SignUpResult result) {
                        UserUtil.setToken(result.token);
                        UserUtil.setUUID(result.uuid);
                        UserUtil.setSecret(result.secret);

                        // 跳转到主页面
                        ARouter.getInstance().build(ARouterPaths.MAIN_ACTIVITY).navigation();
                    }
                });
    }
}

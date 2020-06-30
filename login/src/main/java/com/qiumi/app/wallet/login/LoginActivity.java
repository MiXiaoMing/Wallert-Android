package com.qiumi.app.wallet.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.aoglrithm.Random;
import com.appframe.utils.string.VerifyPhoneNumber;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.login.api.LoginDataManager;
import com.qiumi.app.wallet.login.api.input.SignInBody;
import com.qiumi.app.wallet.login.api.output.SignInResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 登录页面
 */
@Route(path = ARouterPaths.LOGIN_ACTIVITY)
public class LoginActivity extends AutoBaseActivity {

    private EditText etTelephone, etPassword;
    private ImageView ivPasswordOption;
    private TextView tvLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);

        TextView tvLogin = findViewById(R.id.tvLogin);

        rlyBack.setOnClickListener(clickListener);
        tvLogin.setOnClickListener(clickListener);

        LinearLayout llLcation = findViewById(R.id.ll_location);
        tvLocation = findViewById(R.id.tv_location);
        ivPasswordOption = findViewById(R.id.iv_password_option);

        llLcation.setOnClickListener(clickListener);
        ivPasswordOption.setOnClickListener(clickListener);

        TextView tvRetrievePassword = findViewById(R.id.tv_retrieve_password);
        tvRetrievePassword.setOnClickListener(clickListener);

        TextView tvRegister = findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(clickListener);

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
            } else if (viewId == R.id.tvLogin) {
                String areaCode = tvLocation.getText().toString().trim();
                if (TextUtils.isEmpty(areaCode)) {
                    AFToast.showShort(LoginActivity.this, R.string.mobile_area_code_empty);
                    return;
                }

                String telephone = etTelephone.getText().toString().trim();
                if (TextUtils.isEmpty(telephone)) {
                    AFToast.showShort(LoginActivity.this, R.string.mobile_empty);
                    return;
                } else if (!VerifyPhoneNumber.isAvailable(areaCode, telephone)) {
                    AFToast.showShort(LoginActivity.this, R.string.mobile_not_available);
                    return;
                }

                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    AFToast.showShort(LoginActivity.this, R.string.password_empty);
                    return;
                }

                login(areaCode, telephone, password);
            } else if (viewId == R.id.ll_location) {
                showLocationOption();
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
            } else if (viewId == R.id.tv_retrieve_password) {
                startActivity(new Intent(LoginActivity.this, RetrieveActivity.class));
            } else if (viewId == R.id.tv_register) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        }
    };

    /**
     * 选择手机号归属地
     */
    private void showLocationOption() {
        startActivityForResult(new Intent(LoginActivity.this, SearchActivity.class), SearchActivity.SELECT_CITY_CODE);
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

    private void login(final String areaCode, final String mobile, String password) {
        SignInBody body = new SignInBody();
        body.countryCode = areaCode;
        body.mobile = mobile;
        body.password = password;
        body.timestamp = String.valueOf(System.currentTimeMillis());
        body.nonce = Random.getUUID();

        Map<String, String> signParams = new HashMap<>();
        signParams.put("mobile", mobile);
        signParams.put("password", password);
        signParams.put("timestamp", body.timestamp);
        signParams.put("nonce", body.nonce);
        body.sig = SignUtil.generate(signParams, Constants.PREKEY);

        new LoginDataManager()
                .signIn(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<SignInResult>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(SignInResult result) {
                        UserUtil.setTelephoneFull(mobile);
                        UserUtil.setToken(result.getData().token);
                        UserUtil.setUserID(result.getData().userId);
                        UserUtil.setSecret(result.getData().secret);
                        UserUtil.setCountryCode(areaCode);

                        // TODO: 2020/3/12 需要返回secret存储时间
                        UserUtil.setSecretExpire(Long.MAX_VALUE);

                        // 跳转到主页面
                        ARouter.getInstance().build(ARouterPaths.MAIN_ACTIVITY).navigation();
                    }
                });
    }
}

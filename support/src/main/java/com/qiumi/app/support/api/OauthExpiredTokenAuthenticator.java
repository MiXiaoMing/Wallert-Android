package com.qiumi.app.support.api;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.utils.aoglrithm.Random;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.input.RefreshTokenBody;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.support.api.iterfaces.SupportDataManager;
import com.qiumi.app.support.api.output.CountryListResult;
import com.qiumi.app.support.api.output.RefreshTokenResult;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * token过期
 */
public class OauthExpiredTokenAuthenticator implements Authenticator {

    @Override
    public Request authenticate(Route route, final Response response) {
        // 发送Token过期消息
        if (System.currentTimeMillis() > UserUtil.getSecretExpire()) {
            Logger.getLogger().e("token过期，secret过期，跳转登录");
            UserUtil.clear();
            ARouter.getInstance().build(ARouterPaths.LOGIN_ACTIVITY).navigation();
        } else {
            Logger.getLogger().e("token过期，secret有效，刷新token");
            refreshToken();
        }

        return null;
    }


    // 刷新token
    private void refreshToken() {
        RefreshTokenBody body = new RefreshTokenBody();
        body.username = UserUtil.getCountryCode() + UserUtil.getTelephoneFull();
        body.timestamp = String.valueOf(System.currentTimeMillis());
        body.nonce = Random.getUUID();

        Map<String, String> signParams = new HashMap<>();
        signParams.put("username", body.username);
        signParams.put("timestamp", body.timestamp);
        signParams.put("nonce", body.nonce);
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new SupportDataManager()
                .refreshToken(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<RefreshTokenResult>() {

                    @Override
                    public void onError() {
                        UserUtil.clear();
                        ARouter.getInstance().build(ARouterPaths.LOGIN_ACTIVITY).navigation();
                    }

                    @Override
                    public void onSuccess(RefreshTokenResult result) {
                        UserUtil.setToken(result.token);
                        UserUtil.setSecretExpire(result.expire * 1000 + System.currentTimeMillis());
                    }
                });
    }
}


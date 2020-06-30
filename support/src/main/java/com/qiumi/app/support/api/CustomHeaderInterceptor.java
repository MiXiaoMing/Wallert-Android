package com.qiumi.app.support.api;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.qiumi.app.support.utils.UserUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomHeaderInterceptor implements Interceptor {
    /**
     * 添加header
     */
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("Accept", "application/json");

        if (!TextUtils.isEmpty(UserUtil.getToken())) {
            builder.addHeader("token", UserUtil.getToken());
        }

        if (!TextUtils.isEmpty(UserUtil.getSecret())) {
            builder.addHeader("secret", UserUtil.getSecret());
        }

        return chain.proceed(builder.build());
    }
}
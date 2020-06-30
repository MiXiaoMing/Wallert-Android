package com.qiumi.app.support.api.iterfaces;

import com.appframe.framework.http.EmptyHttpResult;
import com.qiumi.app.support.api.MobileServerRetrofit;
import com.qiumi.app.support.api.input.RefreshTokenBody;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.support.api.output.CountryListResult;
import com.qiumi.app.support.api.output.RefreshTokenResult;

import io.reactivex.Observable;
import retrofit2.http.Body;

/**
 * 支撑库 服务
 */
public class SupportDataManager {
    private SupportService service;

    public SupportDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(SupportService.class);
    }

    /**********  登录相关   **********/

    public Observable<RefreshTokenResult> refreshToken(RefreshTokenBody body) {
        return service.refreshToken(body);
    }

}

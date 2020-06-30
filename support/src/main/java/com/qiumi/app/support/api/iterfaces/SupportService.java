package com.qiumi.app.support.api.iterfaces;

import com.qiumi.app.support.api.input.RefreshTokenBody;
import com.qiumi.app.support.api.output.RefreshTokenResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 与设置相关接口
 */
public interface SupportService {



    /**********  登录  **********/

    /**
     * 刷新token
     * @return
     */
    @POST("security/refreshToken")
    Observable<RefreshTokenResult> refreshToken(@Body RefreshTokenBody body);

}

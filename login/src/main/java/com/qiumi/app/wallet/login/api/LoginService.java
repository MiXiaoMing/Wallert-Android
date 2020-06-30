package com.qiumi.app.wallet.login.api;

import com.appframe.framework.http.EmptyHttpResult;
import com.qiumi.app.wallet.login.api.input.RetrieveSmsCodeBody;
import com.qiumi.app.wallet.login.api.input.SignInBody;
import com.qiumi.app.wallet.login.api.input.SignUpBody;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.wallet.login.api.input.UpdatePassBody;
import com.qiumi.app.support.api.output.CountryListResult;
import com.qiumi.app.wallet.login.api.output.SignInResult;
import com.qiumi.app.wallet.login.api.output.SignUpResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 与登录相关接口
 */
public interface LoginService {


    /**********  其他  **********/

    /**
     * 获取 验证码
     * @return
     */
    @GET("getCountryList")
    Observable<CountryListResult> countryList();


    /**********  注册  **********/

    /**
     * 获取 验证码
     * @return
     */
    @POST("smsCode/signUp")
    Observable<EmptyHttpResult> smsCode(@Body SmsCodeBody body);

    /**
     * 注册
     */
    @POST("signUp")
    Observable<SignUpResult> signUp(@Body SignUpBody body);

    /**
     * 登录
     */
    @POST("signIn")
    Observable<SignInResult> signIn(@Body SignInBody body);

    /**
     * 修改密码 获取验证码
     */
    @GET("smsCode/upPass")
    Observable<EmptyHttpResult> retrieveSmsCode(@Query("body") RetrieveSmsCodeBody body);


    /**
     * 修改密码 获取验证码
     */
    @GET("security/updatePass")
    Observable<EmptyHttpResult> updatePass(@Query("body") UpdatePassBody body);
}

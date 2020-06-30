package com.qiumi.app.wallet.mine.api.iterfaces;

import com.appframe.framework.http.EmptyHttpResult;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.support.api.output.CountryListResult;
import com.qiumi.app.wallet.mine.api.input.CreateBioSessionBody;
import com.qiumi.app.wallet.mine.api.input.Kyc2SubmitBody;
import com.qiumi.app.wallet.mine.api.input.SecurityLevelBody;
import com.qiumi.app.wallet.mine.api.input.SetPayPassBody;
import com.qiumi.app.wallet.mine.api.input.SubmitMainlandBody;
import com.qiumi.app.wallet.mine.api.input.UpdatePassBody;
import com.qiumi.app.wallet.mine.api.input.UpdateSettingBody;
import com.qiumi.app.wallet.mine.api.output.CreateBioSessionEntity;
import com.qiumi.app.wallet.mine.api.output.SecurityLevelEntity;
import com.qiumi.app.wallet.mine.api.output.SettingInfoEntity;
import com.qiumi.app.wallet.mine.api.output.UpgradeEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 与设置相关接口
 */
public interface MineService {



    /**********  更改密码  **********/

    /**
     * 获取验证码
     * @return
     */
    @POST("smsCode/setPayPass")
    Observable<EmptyHttpResult> smsCode_Pay(@Body SmsCodeBody body);

    /**
     * 设置支付密码
     * @return
     */
    @POST("security/setPayPass")
    Observable<EmptyHttpResult> setPayPass(@Body SetPayPassBody body);

    /**
     * 更新支付密码
     * @return
     */
    @GET("security/setPayPass")
    Observable<EmptyHttpResult> updatePayCode();

    /**
     * 获取验证码  登录
     * @return
     */
    @POST("smsCode/updatePass")
    Observable<EmptyHttpResult> smsCode_Login();

    /**
     * 设置登录密码
     * @return
     */
    @GET("security/updatePass")
    Observable<EmptyHttpResult> updateLoginPassword(@Body UpdatePassBody body);


    /**********  身份认证  **********/

    /**
     * 获取当前用户的安全级别
     * @return
     */
    @POST("security/level")
    Observable<SecurityLevelEntity> securityLevel(@Body SecurityLevelBody body);

    /**
     * 提交身份认证请求
     * @return
     */
    @GET("security/verifyIDCards")
    Observable<EmptyHttpResult> verifyIDCards();

    /**
     * 提交身份认证请求
     * @return
     */
    @GET("security/abroadVerifyIDCards")
    Observable<EmptyHttpResult> abroadVerifyIDCards();

    /**
     * 提交基本身份认证
     * @return
     */
    @POST("security/kyc/1/submit/mainland")
    Observable<EmptyHttpResult> submitMainland(@Body SubmitMainlandBody body);

    /**
     * 提交高级身份认证请求
     * @return
     */
    @GET("security/abroadHighLevelVerify")
    Observable<EmptyHttpResult> abroadHighLevelVerify();

    /**
     * 提交高级身份认证请求
     * @return
     */
    @POST("security/kyc/2/creatBioSession")
    Observable<CreateBioSessionEntity> createBioSession(@Body CreateBioSessionBody body);

    /**
     * 提交高级身份认证请求
     * @return
     */
    @POST("security/kyc/2/submit/mainland")
    Observable<EmptyHttpResult> kyc2Submit(@Body Kyc2SubmitBody body);




    /**********  其他  **********/

    /**
     * 获取 验证码
     * @return
     */
    @GET("getCountryList")
    Observable<CountryListResult> countryList();


    /**********  系统设置  **********/

    /**
     * 获取 系统设置信息
     * @return
     */
    @POST("setting/info")
    Observable<SettingInfoEntity> settingInfo();

    /**
     * 获取 系统设置信息
     * @return
     */
    @POST("setting/update")
    Observable<EmptyHttpResult> updateSetting(@Body UpdateSettingBody body);

    /**
     * 获取 更新版本
     * @return
     */
    @GET("setting/update")
    Observable<UpgradeEntity> upgrade();

}

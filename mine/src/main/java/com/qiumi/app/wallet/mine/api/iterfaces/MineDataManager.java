package com.qiumi.app.wallet.mine.api.iterfaces;

import com.appframe.framework.http.EmptyHttpResult;
import com.qiumi.app.support.api.MobileServerRetrofit;
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

/**
 * 设置服务
 */
public class MineDataManager {
    private MineService service;

    public MineDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(MineService.class);
    }

    /**********  支付密码  **********/

    public Observable<EmptyHttpResult> smsCode_Pay(SmsCodeBody body) {
        return service.smsCode_Pay(body);
    }

    public Observable<EmptyHttpResult> smsCode_Login() {
        return service.smsCode_Login();
    }

    public Observable<EmptyHttpResult> setPayPass(@Body SetPayPassBody body) {
        return service.setPayPass(body);
    }

    public Observable<EmptyHttpResult> updatePayCode() {
        return service.updatePayCode();
    }

    public Observable<EmptyHttpResult> updateLoginPassword(UpdatePassBody body) {
        return service.updateLoginPassword(body);
    }

    /**********  身份认证  **********/

    public Observable<SecurityLevelEntity> securityLevel(SecurityLevelBody body) {
        return service.securityLevel(body);
    }

    public Observable<EmptyHttpResult> verifyIDCards() {
        return service.verifyIDCards();
    }

    public Observable<EmptyHttpResult> abroadVerifyIDCards() {
        return service.abroadVerifyIDCards();
    }

    public Observable<EmptyHttpResult> abroadHighLevelVerify() {
        return service.abroadHighLevelVerify();
    }

    public Observable<CreateBioSessionEntity> createBioSession(CreateBioSessionBody body) {
        return service.createBioSession(body);
    }

    public Observable<EmptyHttpResult> submitMainland(SubmitMainlandBody body) {
        return service.submitMainland(body);
    }

    public Observable<EmptyHttpResult> kyc2Submit(Kyc2SubmitBody body) {
        return service.kyc2Submit(body);
    }

    /**********  其他  **********/

    public Observable<CountryListResult> countryList() {
        return service.countryList();
    }

    /**********  系统设置  **********/

    public Observable<SettingInfoEntity> settingInfo() {
        return service.settingInfo();
    }

    public Observable<EmptyHttpResult> updateSetting(UpdateSettingBody body) {
        return service.updateSetting(body);
    }

    public Observable<UpgradeEntity> upgrade() {
        return service.upgrade();
    }
}

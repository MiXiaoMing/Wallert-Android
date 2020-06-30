package com.qiumi.app.wallet.login.api;

import com.appframe.framework.http.EmptyHttpResult;
import com.qiumi.app.support.api.MobileServerRetrofit;
import com.qiumi.app.wallet.login.api.input.RetrieveSmsCodeBody;
import com.qiumi.app.wallet.login.api.input.SignInBody;
import com.qiumi.app.wallet.login.api.input.SignUpBody;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.wallet.login.api.input.UpdatePassBody;
import com.qiumi.app.support.api.output.CountryListResult;
import com.qiumi.app.wallet.login.api.output.SignInResult;
import com.qiumi.app.wallet.login.api.output.SignUpResult;

import io.reactivex.Observable;

/**
 * 登录
 */
public class LoginDataManager {
    private LoginService service;

    public LoginDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(LoginService.class);
    }

    public Observable<CountryListResult> countryList() {
        return service.countryList();
    }

    /**********  登录  **********/

    public Observable<EmptyHttpResult> smsCode(SmsCodeBody body) {
        return service.smsCode(body);
    }

    public Observable<SignUpResult> signUp(SignUpBody body) {
        return service.signUp(body);
    }

    public Observable<SignInResult> signIn(SignInBody body) {
        return service.signIn(body);
    }

    public Observable<EmptyHttpResult> retrieveSmsCode(RetrieveSmsCodeBody body) {
        return service.retrieveSmsCode(body);
    }

    public Observable<EmptyHttpResult> updatePass(UpdatePassBody body) {
        return service.updatePass(body);
    }
}

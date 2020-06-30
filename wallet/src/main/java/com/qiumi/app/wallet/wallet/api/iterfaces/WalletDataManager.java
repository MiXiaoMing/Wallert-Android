package com.qiumi.app.wallet.wallet.api.iterfaces;

import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.framework.http.HttpResult;
import com.qiumi.app.support.api.MobileServerRetrofit;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.wallet.wallet.api.input.AssetsRecordBody;
import com.qiumi.app.wallet.wallet.api.input.DepositAddressBody;
import com.qiumi.app.wallet.wallet.api.input.LoginBody;
import com.qiumi.app.wallet.wallet.api.input.NewAddressBody;
import com.qiumi.app.wallet.wallet.api.input.RecordDetailBody;
import com.qiumi.app.wallet.wallet.api.input.SubmitWithDrawBody;
import com.qiumi.app.wallet.wallet.api.input.AllAmountBody;
import com.qiumi.app.wallet.wallet.api.input.FeeBody;
import com.qiumi.app.wallet.wallet.api.input.TransferOutBody;
import com.qiumi.app.wallet.wallet.api.output.AmountEntity;
import com.qiumi.app.wallet.wallet.api.output.AssetsEntity;
import com.qiumi.app.wallet.wallet.api.output.DepositeAddressEntity;
import com.qiumi.app.wallet.wallet.api.output.NewAddressResult;
import com.qiumi.app.wallet.wallet.api.output.RecordDetailEntity;
import com.qiumi.app.wallet.wallet.api.output.RecordsEntity;
import com.qiumi.app.wallet.wallet.api.output.TransferSubmitEntity;
import com.qiumi.app.wallet.wallet.api.output.UserInfoEntity;

import io.reactivex.Observable;

/**
 * 钱包服务
 */
public class WalletDataManager {
    private WalletService service;

    public WalletDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(WalletService.class);
    }

    /**********  用户  **********/

    public Observable<UserInfoEntity> userInfo() {
        return service.userInfo(new LoginBody());
    }


    /**********  资产  **********/

    public Observable<AssetsEntity> assets() {
        return service.assets();
    }

    public Observable<RecordsEntity> getRecords(AssetsRecordBody body) {
        return service.records(body);
    }

    public Observable<HttpResult<RecordDetailEntity>> getRecordDetail(RecordDetailBody body) {
        return service.recordDetail(body);
    }


    /**********  提现  **********/

    public Observable<AmountEntity> getAllMount_WithDraw(AllAmountBody body) {
        return service.getAllAmount_WithDraw(body);
    }

    public Observable<AmountEntity> getFee_WithDraw(FeeBody body) {
        return service.getFee_WithDraw(body);
    }

    public Observable<EmptyHttpResult> smsCode_WithDraw(SmsCodeBody body) {
        return service.smsCode_WithDraw(body);
    }

    public Observable<NewAddressResult> newAddress(NewAddressBody body) {
        return service.newAddress(body);
    }

    public Observable<TransferSubmitEntity> submit_WithDraw(SubmitWithDrawBody body) {
        return service.submit_WithDraw(body);
    }

    /**********  充值  **********/

    public Observable<DepositeAddressEntity> getAddress(DepositAddressBody body) {
        return service.getAddress(body);
    }

    public Observable<DepositeAddressEntity> getAddressWithAmount() {
        return service.getAddressWithAmount();
    }

    /**********  转账  **********/

    public Observable<AmountEntity> getAllMount_Transfer(AllAmountBody body) {
        return service.getAllAmount_Transfer(body);
    }

    public Observable<AmountEntity> getFee_Transfer(FeeBody body) {
        return service.getFee_Transfer(body);
    }

    public Observable<EmptyHttpResult> newAccount(String telphone) {
        return service.newAccount();
    }

    public Observable<EmptyHttpResult> smsCode_Transfer(SmsCodeBody body) {
        return service.smsCode_Transfer(body);
    }

    public Observable<TransferSubmitEntity> transferSubmit(TransferOutBody body) {
        return service.transferSubmit(body);
    }

    public Observable<TransferSubmitEntity> transferScanSubmit(SubmitWithDrawBody body) {
        return service.transferScanSubmit(body);
    }
}

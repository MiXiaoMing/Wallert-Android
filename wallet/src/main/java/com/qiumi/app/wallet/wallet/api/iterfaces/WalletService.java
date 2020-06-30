package com.qiumi.app.wallet.wallet.api.iterfaces;

import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.framework.http.HttpResult;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.wallet.wallet.api.input.AssetsRecordBody;
import com.qiumi.app.wallet.wallet.api.input.DepositAddressBody;
import com.qiumi.app.wallet.wallet.api.input.LoginBody;
import com.qiumi.app.wallet.wallet.api.input.NewAddressBody;
import com.qiumi.app.wallet.wallet.api.input.RecordDetailBody;
import com.qiumi.app.wallet.wallet.api.input.AllAmountBody;
import com.qiumi.app.wallet.wallet.api.input.FeeBody;
import com.qiumi.app.wallet.wallet.api.input.SubmitWithDrawBody;
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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 与钱包相关接口
 */
public interface WalletService {

    /**********  用户  **********/

    /**
     * 获取用户基本信息
     */
    @POST("userInfo/simple")
    Observable<UserInfoEntity> userInfo(@Body LoginBody body);


    /**********  资产  **********/

    /**
     * 获取总资产
     */
    @POST("userAssets/simple")
    Observable<AssetsEntity> assets();

    /**
     * 获取资产记录
     */
    @POST("userAssets/records")
    Observable<RecordsEntity> records(@Body AssetsRecordBody body);

    /**
     * 获取资产记录详情
     */
    @POST("userAssets/recordDetail")
    Observable<HttpResult<RecordDetailEntity>> recordDetail(@Body RecordDetailBody body);



    /**********  提现  **********/

    /**
     * 获取全部额度的转账手续费
     * @return
     */
    @POST("withdraw/allAmount")
    Observable<AmountEntity> getAllAmount_WithDraw(@Body AllAmountBody body);

    /**
     * 获取指定额度的转账手续费
     * @return
     */
    @POST("withdraw/fee")
    Observable<AmountEntity> getFee_WithDraw(@Body FeeBody body);

    /**
     * 获取提现验证码
     * @return
     */
    @POST("smsCode/withdraw")
    Observable<EmptyHttpResult> smsCode_WithDraw(@Body SmsCodeBody body);

    /**
     * 判断 是否是新地址
     * @return
     */
    @POST("withdraw/newAddrCheck")
    Observable<NewAddressResult> newAddress(@Body NewAddressBody body);

    /**
     * 提交 申请
     * @return
     */
    @POST("withdraw/submit")
    Observable<TransferSubmitEntity> submit_WithDraw(@Body SubmitWithDrawBody body);


    /**********  充值  **********/

    /**
     * 获取充值地址
     * @return
     */
    @POST("deposit/address")
    Observable<DepositeAddressEntity> getAddress(@Body DepositAddressBody body);

    /**
     * 获取指定金额充值地址
     * @return
     */
    @GET("deposite/getAddr")
    Observable<DepositeAddressEntity> getAddressWithAmount();


    /**********  转账  **********/

    /**
     * 获取全部额度的转账手续费
     * @return
     */
    @POST("transfer/allAmount")
    Observable<AmountEntity> getAllAmount_Transfer(@Body AllAmountBody body);

    /**
     * 获取该额度的转账手续费
     * @return
     */
    @POST("transfer/fee")
    Observable<AmountEntity> getFee_Transfer(@Body FeeBody body);

    /**
     * 获取提现验证码
     * @return
     */
    @POST("smsCode/transfer")
    Observable<EmptyHttpResult> smsCode_Transfer(@Body SmsCodeBody body);

    /**
     * 是否为平台账号
     * @return
     */
    @POST("transfer/newAccount")
    Observable<EmptyHttpResult> newAccount();

    /**
     * 提交转账申请
     * @return
     */
    @POST("transfer/submit/fast")
    Observable<TransferSubmitEntity> transferSubmit(@Body TransferOutBody body);

    /**
     * 提交转账申请
     * @return
     */
    @POST("transfer/submit")
    Observable<TransferSubmitEntity> transferScanSubmit(@Body SubmitWithDrawBody body);
}

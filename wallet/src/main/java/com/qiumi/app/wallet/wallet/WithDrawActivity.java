package com.qiumi.app.wallet.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.aoglrithm.Random;
import com.appframe.utils.logger.Logger;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.api.input.SmsCodeBody;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.wallet.api.input.AllAmountBody;
import com.qiumi.app.wallet.wallet.api.input.FeeBody;
import com.qiumi.app.wallet.wallet.api.input.NewAddressBody;
import com.qiumi.app.wallet.wallet.api.input.SubmitWithDrawBody;
import com.qiumi.app.wallet.wallet.api.iterfaces.WalletDataManager;
import com.qiumi.app.wallet.wallet.api.output.AmountEntity;
import com.qiumi.app.wallet.wallet.api.output.Asset;
import com.qiumi.app.wallet.wallet.api.output.NewAddressResult;
import com.qiumi.app.wallet.wallet.api.output.TransferSubmitEntity;
import com.qiumi.app.wallet.wallet.view.SecurityVerifyDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WithDrawActivity extends AutoBaseActivity {

    private String id, name;

    private EditText etNumber, etPwd, etPhoneConfirm;
    private TextView tvCoinName, tvCoinNameFee, tvFee, tvPhoneConfirm, tvWithdraw;
    private LinearLayout llyPhoneConfirm;

    private TextView ftvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);

        id = getIntent().getStringExtra("currencyId");
        name = getIntent().getStringExtra("name");

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name))
            finish();

        initView();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.withdraw) + name);

        RelativeLayout rlyBack = findViewById(R.id.rlyBack);
        TextView tvTransferOutRecord = findViewById(R.id.tvTransferOutRecord);

        etNumber = findViewById(R.id.etNumber);
        tvCoinName = findViewById(R.id.tvCoinName);
        tvFee = findViewById(R.id.tvFee);
        tvCoinNameFee = findViewById(R.id.tvCoinNameFee);

        etPwd = findViewById(R.id.etPwd);
        etPhoneConfirm = findViewById(R.id.etPhoneConfirm);
        tvPhoneConfirm = findViewById(R.id.tvPhoneConfirm);

        LinearLayout llyScan = findViewById(R.id.llyScan);
        LinearLayout llyAll = findViewById(R.id.llyAll);
        llyPhoneConfirm = findViewById(R.id.llyPhoneConfirm);

        ftvAddress = findViewById(R.id.ftvAddress);

        tvWithdraw = findViewById(R.id.tvWithdraw);

        rlyBack.setOnClickListener(clickListener);
        tvTransferOutRecord.setOnClickListener(clickListener);
        llyScan.setOnClickListener(clickListener);
        llyAll.setOnClickListener(clickListener);
        llyPhoneConfirm.setOnClickListener(clickListener);
        tvWithdraw.setOnClickListener(clickListener);

        etNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String text = etNumber.getText().toString().trim();
                    if (TextUtils.isEmpty(text))
                        return;

                    text = deleteZero(text);
                    etNumber.setText(text);

                    float total = Float.valueOf(String.valueOf(text));
                    if (total > 0) {
                        getFee();
                    } else {
                        etNumber.setText("0");
                    }
                }
            }
        });

        tvCoinName.setText(name);
        tvCoinNameFee.setText(name);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.rlyBack) {
                finish();
            } else if (viewId == R.id.tvTransferOutRecord) {
                Intent intent = new Intent(WithDrawActivity.this, AssetRecordActivity.class);

                List<Asset> assets = new ArrayList<>();
                Asset asset = new Asset();
                asset.currencyId = id;
                asset.name = name;
                assets.add(asset);
                intent.putExtra("assets", (Serializable) assets);

                List<Constants.IDNamePair> operTypes = new ArrayList<>();
                // TODO: 2019/8/26 这里写死了数据，一定要注意哦
                operTypes.add(Constants.operTypes.get(0));
                intent.putExtra("operTypes", (Serializable) operTypes);

                startActivity(intent);
            } else if (viewId == R.id.llyScan) {
                // 创建IntentIntegrator对象
                IntentIntegrator intentIntegrator = new IntentIntegrator(WithDrawActivity.this);
                // 开始扫描
                intentIntegrator.initiateScan();
            } else if (viewId == R.id.llyAll) {
                getAllAmount();
            } else if (viewId == R.id.llyPhoneConfirm) {
                getSmsCode();
            } else if (viewId == R.id.tvWithdraw) {
                newAddress();
            }
        }
    };

    // 展示安全验证框
    private void showSecurityVerity() {
        SecurityVerifyDialog dialog = new SecurityVerifyDialog(this);
        dialog.setOnClickListener(new SecurityVerifyDialog.OnClickListener() {
            @Override
            public void onPositiveClick(String content) {
                submit(content);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取解析结果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Logger.getLogger().d("取消扫描");
            } else {
                Logger.getLogger().d("扫描内容：" + result.getContents());
                ftvAddress.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvPhoneConfirm.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            llyPhoneConfirm.setEnabled(true);
            tvPhoneConfirm.setText("获取验证码");
        }
    };

    private void getFee() {
        FeeBody body = new FeeBody();
        body.coinType.coinId = id;
        body.coinType.coinSymbol = name;

        body.amount = etNumber.getText().toString().trim();

        String amount = deleteZero(body.amount);
        Logger.getLogger().e("费用：" + amount);

        Map<String, String> signParams = new HashMap<>();
        signParams.put("coinId", id);
        signParams.put("token", UserUtil.getToken());
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new WalletDataManager()
                .getFee_WithDraw(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<AmountEntity>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(AmountEntity result) {
                        if (ActivityUtil.isAvailable(WithDrawActivity.this)) {
                            if (result.getData().fee == null) {
                                AFToast.showShort(WithDrawActivity.this, R.string.netword_fail);
                            } else {
                                tvFee.setText(result.getData().fee.fee);
                            }
                        }
                    }
                });
    }

    private void getAllAmount() {
        AllAmountBody body = new AllAmountBody();
        body.coinType.coinId = id;
        body.coinType.coinSymbol = name;

        Map<String, String> signParams = new HashMap<>();
        signParams.put("coinId", id);
        signParams.put("token", UserUtil.getToken());
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new WalletDataManager()
                .getAllMount_WithDraw(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<AmountEntity>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(AmountEntity result) {
                        if (result.getData() == null || result.getData().fee == null || result.getData().amount == null) {
                            if (ActivityUtil.isAvailable(WithDrawActivity.this)) {
                                AFToast.showShort(WithDrawActivity.this, R.string.netword_fail);
                            }
                            return;
                        }

                        tvFee.setText(result.getData().fee.fee);
                        tvCoinNameFee.setText(result.getData().fee.coinSymbol);

                        etNumber.setText(result.getData().amount.amount);
                        tvCoinName.setText(result.getData().amount.coinSymbol);
                    }
                });
    }

    // 获取手机验证码
    private void getSmsCode() {
        SmsCodeBody body = new SmsCodeBody();
        body.mobile = UserUtil.getTelephoneFull();
        body.countryCode = UserUtil.getCountryCode();

        Map<String, String> signParams = new HashMap<>();
        signParams.put("mobile", UserUtil.getTelephoneFull());
        body.sig = SignUtil.generate(signParams, Constants.PREKEY);

        new WalletDataManager()
                .smsCode_WithDraw(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        if (ActivityUtil.isAvailable(WithDrawActivity.this)) {
                            AFToast.showShort(WithDrawActivity.this, R.string.send_smscode_success);

                            llyPhoneConfirm.setEnabled(false);
                            countDownTimer.start();
                        }
                    }
                });
    }

    // 判断是否为新地址
    private void newAddress() {
        final String address = ftvAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            AFToast.showShort(WithDrawActivity.this, R.string.withdraw_address_empty);
            return;
        }

        NewAddressBody body = new NewAddressBody();
        body.toAddr = address;
        body.currencyId = id;

        new WalletDataManager()
                .newAddress(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<NewAddressResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(NewAddressResult result) {
                        if (result.getStatus().equals("1")) {
                            showSecurityVerity();
                        } else {
                            if (ActivityUtil.isAvailable(WithDrawActivity.this)) {
                                AFToast.showShort(WithDrawActivity.this, result.getMessage());
                            }
                            submit(result.idNo);
                        }
                    }
                });
    }

    // 提交 提现申请
    private void submit(String idNo) {
        final String address = ftvAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            AFToast.showShort(WithDrawActivity.this, R.string.withdraw_address_empty);
            return;
        }

        String amount = etNumber.getText().toString().trim();
        if (TextUtils.isEmpty(amount)) {
            if (ActivityUtil.isAvailable(WithDrawActivity.this)) {
                AFToast.showShort(WithDrawActivity.this, "提现金额不能未空");
            }
            return;
        }

        amount = deleteZero(amount);

        String payPass = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(payPass)) {
            if (ActivityUtil.isAvailable(WithDrawActivity.this)) {
                AFToast.showShort(WithDrawActivity.this, "请输入支付密码");
            }
            return;
        }

        String smsCode = etPhoneConfirm.getText().toString().trim();
        if (TextUtils.isEmpty(smsCode)) {
            if (ActivityUtil.isAvailable(WithDrawActivity.this)) {
                AFToast.showShort(WithDrawActivity.this, "请输入验证码");
            }
            return;
        }

        SubmitWithDrawBody body = new SubmitWithDrawBody();
        body.toAddr = address;
        body.idCode = idNo;
        body.timestamp = String.valueOf(System.currentTimeMillis());
        body.nonce = Random.getUUID();
        body.payPass = payPass;
        body.smsCode = smsCode;

        body.amount.amount = amount;
        body.amount.coinId = id;
        body.amount.coinSymbol = name;

        Map<String, String> signParams = new HashMap<>();
        signParams.put("toAddr", body.toAddr);
        signParams.put("amount", body.amount.amount);
        signParams.put("timestamp", body.timestamp);
        signParams.put("nonce", body.nonce);
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new WalletDataManager()
                .submit_WithDraw(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<TransferSubmitEntity>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(TransferSubmitEntity result) {
                        if (ActivityUtil.isAvailable(WithDrawActivity.this)) {
                            AFToast.showShort(WithDrawActivity.this, result.getMessage());
                        }
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private String deleteZero(String origin) {
        String tmp = origin;
        while (true) {
            if (tmp.contains(".") && (tmp.substring(tmp.length() - 1).equals("0")
                || tmp.substring(tmp.length() - 1).equals("."))) {
                Logger.getLogger().e("tmp：" + tmp);
                tmp = tmp.substring(0, tmp.length() - 1);
                Logger.getLogger().e("tmp：" + tmp);
            } else {
                break;
            }
        }
        return tmp;
    }
}

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
import com.qiumi.app.support.component.CustomDialog;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.wallet.api.input.AllAmountBody;
import com.qiumi.app.wallet.wallet.api.input.FeeBody;
import com.qiumi.app.wallet.wallet.api.input.SubmitWithDrawBody;
import com.qiumi.app.wallet.wallet.api.iterfaces.WalletDataManager;
import com.qiumi.app.wallet.wallet.api.output.AmountEntity;
import com.qiumi.app.wallet.wallet.api.output.Asset;
import com.qiumi.app.wallet.wallet.api.output.TransferSubmitEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 扫码转账
 */
public class TransferOutScanActivity extends AutoBaseActivity {

    private String id, name;

    private EditText etNumber, etPwd, etPhoneConfirm;
    private TextView ftvAddress, tvNumberName, tvFeeName, tvFee, tvPhoneConfirm;
    private LinearLayout llyPhoneConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_out_scan);

        id = getIntent().getStringExtra("currencyId");
        name = getIntent().getStringExtra("name");

        initView();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.transfer_out) + name);

        RelativeLayout rlyBack = findViewById(R.id.rlyBack);
        TextView tvTransferOutRecord = findViewById(R.id.tvTransferOutRecord);

        LinearLayout llyScan = findViewById(R.id.llyScan);
        ftvAddress = findViewById(R.id.ftvAddress);

        etNumber = findViewById(R.id.etNumber);
        tvNumberName = findViewById(R.id.tvNumberName);
        tvFee = findViewById(R.id.tvFee);
        tvFeeName = findViewById(R.id.tvFeeName);
        etPwd = findViewById(R.id.etPwd);
        etPhoneConfirm = findViewById(R.id.etPhoneConfirm);
        tvPhoneConfirm = findViewById(R.id.tvPhoneConfirm);

        LinearLayout llyAll = findViewById(R.id.llyAll);
        llyPhoneConfirm = findViewById(R.id.llyPhoneConfirm);

        TextView tvTransferOut = findViewById(R.id.tvTransferOut);

        rlyBack.setOnClickListener(clickListener);
        llyScan.setOnClickListener(clickListener);
        tvTransferOutRecord.setOnClickListener(clickListener);
        llyAll.setOnClickListener(clickListener);
        llyPhoneConfirm.setOnClickListener(clickListener);
        tvTransferOut.setOnClickListener(clickListener);

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
                    }
                }
            }
        });

        tvNumberName.setText(name);
        tvFeeName.setText(name);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.rlyBack) {
                finish();
            } else if (viewId == R.id.llyScan) {
                // 创建IntentIntegrator对象
                IntentIntegrator intentIntegrator = new IntentIntegrator(TransferOutScanActivity.this);
                // 开始扫描
                intentIntegrator.initiateScan();
            }  else if (viewId == R.id.tvTransferOutRecord) {
                Intent intent = new Intent(TransferOutScanActivity.this, AssetRecordActivity.class);

                List<Asset> assets = new ArrayList<>();
                Asset asset = new Asset();
                asset.currencyId = id;
                asset.name = name;
                assets.add(asset);
                intent.putExtra("assets", (Serializable) assets);

                List<Constants.IDNamePair> operTypes = new ArrayList<>();
                operTypes.add(Constants.operTypes.get(2));
                intent.putExtra("operTypes", (Serializable) operTypes);

                startActivity(intent);
            } else if (viewId == R.id.llyAll) {
                getAllAmount();
            } else if (viewId == R.id.llyPhoneConfirm) {
                getSmsCode();
            } else if (viewId == R.id.tvTransferOut) {
                submit();
            }
        }
    };

    private void securityVerity() {
        CustomDialog dialog = new CustomDialog(this);
        dialog.setTitle(getResources().getString(R.string.is_transfer_out_title))
                .setMessage(getResources().getString(R.string.is_transfer_out_content))
                .setOnPositiveClickListener(new CustomDialog.OnPositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        submit();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void getFee() {
        FeeBody body = new FeeBody();
        body.coinType.coinId = id;
        body.coinType.coinSymbol = name;

        body.amount = etNumber.getText().toString().trim();

        Map<String, String> signParams = new HashMap<>();
        signParams.put("coinId", id);
        signParams.put("token", UserUtil.getToken());
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new WalletDataManager()
                .getFee_Transfer(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<AmountEntity>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(AmountEntity result) {
                        if (ActivityUtil.isAvailable(TransferOutScanActivity.this) && result.getData().fee != null) {
                            tvFee.setText(result.getData().fee.fee);
                        }
                    }
                });
    }

    /**
     * 获取全部金额手续费
     */
    private void getAllAmount() {
        AllAmountBody body = new AllAmountBody();
        body.coinType.coinId = id;
        body.coinType.coinSymbol = name;

        Map<String, String> signParams = new HashMap<>();
        signParams.put("coinId", id);
        signParams.put("token", UserUtil.getToken());
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new WalletDataManager()
                .getAllMount_Transfer(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<AmountEntity>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(AmountEntity result) {
                        if (result.getData().fee == null || result.getData().amount == null) {
                            Logger.getLogger().e("获取额度转账费用, fee或amount为空");
                            return;
                        }

                        tvFee.setText(result.getData().fee.fee);
                        etNumber.setText(result.getData().amount.amount);
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
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new WalletDataManager()
                .smsCode_Transfer(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        if (ActivityUtil.isAvailable(TransferOutScanActivity.this)) {
                            AFToast.showShort(TransferOutScanActivity.this, R.string.send_smscode_success);

                            llyPhoneConfirm.setEnabled(false);
                            countDownTimer.start();
                        }
                    }
                });
    }

    // 提交 提现申请
    private void submit() {
        final String address = ftvAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            AFToast.showShort(TransferOutScanActivity.this, R.string.withdraw_address_empty);
            return;
        }

        String amount = etNumber.getText().toString().trim();
        if (TextUtils.isEmpty(amount)) {
            if (ActivityUtil.isAvailable(TransferOutScanActivity.this)) {
                AFToast.showShort(TransferOutScanActivity.this, "转账金额不能未空");
            }
            return;
        }

        amount = deleteZero(amount);

        String payPass = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(payPass)) {
            if (ActivityUtil.isAvailable(TransferOutScanActivity.this)) {
                AFToast.showShort(TransferOutScanActivity.this, "请输入支付密码");
            }
            return;
        }

        String smsCode = etPhoneConfirm.getText().toString().trim();
        if (TextUtils.isEmpty(smsCode)) {
            if (ActivityUtil.isAvailable(TransferOutScanActivity.this)) {
                AFToast.showShort(TransferOutScanActivity.this, "请输入验证码");
            }
            return;
        }

        SubmitWithDrawBody body = new SubmitWithDrawBody();
        body.toAddr = address;
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
                .transferScanSubmit(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<TransferSubmitEntity>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(TransferSubmitEntity result) {
                        if (ActivityUtil.isAvailable(TransferOutScanActivity.this)) {
                            AFToast.showShort(TransferOutScanActivity.this, result.getMessage());
                            if (result.getStatus().equals("1")) {
                                finish();
                            }
                        }
                    }
                });
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

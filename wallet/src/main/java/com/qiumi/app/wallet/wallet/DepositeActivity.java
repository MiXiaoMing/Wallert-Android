package com.qiumi.app.wallet.wallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.wallet.wallet.api.input.DepositAddressBody;
import com.qiumi.app.wallet.wallet.api.iterfaces.WalletDataManager;
import com.qiumi.app.wallet.wallet.api.output.Asset;
import com.qiumi.app.wallet.wallet.api.output.DepositeAddressEntity;
import com.qiumi.app.wallet.wallet.utils.QRCode;
import com.qiumi.app.wallet.wallet.view.MoneyDialog;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DepositeActivity extends AutoBaseActivity {
    private String id, name;
    private TextView tfvInputMoney, ftvTempMoney, ftvAddress, ftvCopyAddress;
    private ImageView ivQRCode;

    private String address, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposite);

        id = getIntent().getStringExtra("currencyId");
        name = getIntent().getStringExtra("name");

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.deposite) + name);

        initView();
        getAddress();
    }

    private void initView() {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);
        TextView tvDepositeRecord = findViewById(R.id.tvDepositeRecord);

        TextView tvCashDeposite = findViewById(R.id.tvCashDeposite);
        tvCashDeposite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        ivQRCode = findViewById(R.id.ivQRCode);
        tfvInputMoney = findViewById(R.id.tfv_input_money);

        rlyBack.setOnClickListener(clickListener);
        tvDepositeRecord.setOnClickListener(clickListener);
        tvCashDeposite.setOnClickListener(clickListener);
        tfvInputMoney.setOnClickListener(clickListener);

        ftvTempMoney = findViewById(R.id.ftvTempMoney);
        ftvAddress = findViewById(R.id.ftvAddress);
        ftvCopyAddress = findViewById(R.id.ftvCopyAddress);

        ftvCopyAddress.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.rlyBack) {
                finish();
            } else if (viewId == R.id.tvDepositeRecord) {
                Intent intent = new Intent(DepositeActivity.this, AssetRecordActivity.class);

                List<Asset> assets = new ArrayList<>();
                Asset asset = new Asset();
                asset.currencyId = id;
                asset.name = name;
                assets.add(asset);
                intent.putExtra("assets", (Serializable) assets);

                List<Constants.IDNamePair> operTypes = new ArrayList<>();
                // TODO: 2019/8/26 这里写死了数据，一定要注意哦
                operTypes.add(Constants.operTypes.get(1));
                intent.putExtra("operTypes", (Serializable) operTypes);

                startActivity(intent);
            } else if (viewId == R.id.tvCashDeposite) {
                AFToast.showShort(DepositeActivity.this, R.string.waiting);
            } else if (viewId == R.id.tfv_input_money) {
                MoneyDialog moneyDialog = new MoneyDialog();
                moneyDialog.setType(name);
                moneyDialog.setOnClickerListener(new MoneyDialog.OnClickListener() {
                    @Override
                    public void onClickSure(String value) {
                        ftvTempMoney.setVisibility(View.VISIBLE);
                        ftvTempMoney.setText(MessageFormat.format(getString(R.string.receive_money_value) + name, value));
                        tfvInputMoney.setText(getString(R.string.change_money));

                        amount = value;
                    }
                });
                moneyDialog.show(getFragmentManager(), "money_dialog");
            } else if (viewId == R.id.ftvCopyAddress) {
                if (!TextUtils.isEmpty(address)) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = null;
                    if (!TextUtils.isEmpty(amount)) {
                        clipData = ClipData.newPlainText("Lable", address + "%amount=" + amount);
                    } else {
                        clipData = ClipData.newPlainText("Lable", address);
                    }
                    clipboardManager.setPrimaryClip(clipData);
                    AFToast.showShort(DepositeActivity.this, R.string.copy_content_to_clipboard);
                }
            }
        }
    };

    private void getAddress() {
        DepositAddressBody body = new DepositAddressBody();
        body.coinId = id;

        new WalletDataManager()
                .getAddress(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<DepositeAddressEntity>() {
                    @Override
                    public void onError() {
                        if (ActivityUtil.isAvailable(DepositeActivity.this)) {
                            AFToast.showShort(DepositeActivity.this, R.string.netword_fail);
                            finish();
                        }
                    }

                    @Override
                    public void onSuccess(DepositeAddressEntity result) {
                        if (result.getData() == null) {
                            Logger.getLogger().e("获取充值地址, data == null");
                            return;
                        }

                        if (TextUtils.isEmpty(result.getData().addr)) {
                            Logger.getLogger().e("获取充值地址, data.addr == null");
                            return;
                        }

                        if (!TextUtils.isEmpty(result.getData().addr)) {
                            address = result.getData().addr;
                            ivQRCode.setImageBitmap(QRCode.createQRCodeWithLogo(result.getData().addr, AutoUtils.getPercentWidthSize(178),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.icon_app)));
                            ftvAddress.setText(result.getData().addr);
                        } else {
                            ivQRCode.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}

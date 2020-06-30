package com.qiumi.app.wallet.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.framework.http.HttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.ClipboardUtil;
import com.appframe.utils.logger.Logger;
import com.google.gson.Gson;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.wallet.api.input.RecordDetailBody;
import com.qiumi.app.wallet.wallet.api.iterfaces.WalletDataManager;
import com.qiumi.app.wallet.wallet.api.output.RecordDetailEntity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RecordDetailActivity extends AutoBaseActivity {

    private TextView tvWithdrawAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        String transNo = getIntent().getStringExtra("transNo");
        String operType = getIntent().getStringExtra("operType");

        getDetail(transNo, operType);
    }

    private void initView(RecordDetailEntity entity) {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);

        TextView tvAmount = findViewById(R.id.tvAmount);
        TextView tvType = findViewById(R.id.tvType);

        TextView tvCreateTime = findViewById(R.id.tvCreateTime);
        tvWithdrawAddr = findViewById(R.id.tvWithdrawAddr);
        TextView tvCharge = findViewById(R.id.tvCharge);
        TextView tvTXID = findViewById(R.id.tvTXID);
        TextView tvChain = findViewById(R.id.tvChain);

        LinearLayout llyCopy = findViewById(R.id.llyCopy);
        LinearLayout llyRight = findViewById(R.id.llyRight);


        rlyBack.setOnClickListener(clickListener);
        llyCopy.setOnClickListener(clickListener);
        llyRight.setOnClickListener(clickListener);

        if (entity.amount.amount <= 0) {
            tvAmount.setTextColor(getResources().getColor(R.color.black_33));
        } else {
            tvAmount.setTextColor(getResources().getColor(R.color.yellow_d6));
        }
        tvAmount.setText(String.valueOf(entity.amount.amount));
        tvType.setText(entity.statusMsg);
        tvCreateTime.setText(entity.dateTime);
        tvWithdrawAddr.setText(entity.from.addr);
        tvCharge.setText(entity.fee.fee + entity.fee.coinSymbol);
        tvTXID.setText(entity.TXID);
        tvChain.setText(entity.blockNo);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.rlyBack) {
                finish();
            } else if (viewId == R.id.llyCopy) {
                String address = tvWithdrawAddr.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    AFToast.showShort(RecordDetailActivity.this, R.string.data_empty);
                    return;
                }
                if (ClipboardUtil.copy(RecordDetailActivity.this, tvWithdrawAddr.getText().toString().trim())) {
                    AFToast.showShort(RecordDetailActivity.this, R.string.copy_success);
                }
            } else if (viewId == R.id.llyRight) {
                AFToast.showShort(RecordDetailActivity.this, R.string.waiting);
            }
        }
    };

    private void getDetail(String transNo, String operType) {
        RecordDetailBody body = new RecordDetailBody();
        body.tranNo = Long.valueOf(transNo);
        body.opType = operType;

        Map<String, String> signParams = new HashMap<>();
        signParams.put("tranNo", transNo);
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new WalletDataManager()
                .getRecordDetail(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<HttpResult<RecordDetailEntity>>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(HttpResult<RecordDetailEntity> result) {
                        if (result.getData() == null) {
                            Logger.getLogger().e("获取资产记录详情, result为空");
                            return;
                        }

                        initView(result.getData());
                    }
                });
    }
}

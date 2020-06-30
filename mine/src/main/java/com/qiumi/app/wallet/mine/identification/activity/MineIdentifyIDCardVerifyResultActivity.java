package com.qiumi.app.wallet.mine.identification.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;
import com.qiumi.app.wallet.mine.identification.dialog.MineIdentificationIDResultDialog;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 基本实名认证页-上传身份证-识别后。
 */

public class MineIdentifyIDCardVerifyResultActivity extends AutoBaseActivity {
    /**
     * 认证结果弹框
     */
    private MineIdentificationIDResultDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_identification_upload_id_card_result);

        findViewById(R.id.rlyBack).setOnClickListener(clickListener);
        findViewById(R.id.tv_submit_identification).setOnClickListener(clickListener);

//        mDialog = new MineIdentificationIDResultDialog(this);
//        // todo dialog根据状态显示文字
//        mDialog.setOnConfirmClickListener(this::actionStartSetPayPasswordActivity);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tv_submit_identification) {
                verifyIDCards();
            } else if (id == R.id.rlyBack) {
                finish();
            }
        }
    };


    /**
     * 提交国内基本信息认证
     */
    private void verifyIDCards() {
        new MineDataManager()
                .verifyIDCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        if (ActivityUtil.isAvailable(MineIdentifyIDCardVerifyResultActivity.this)) {
                            AFToast.showShort(MineIdentifyIDCardVerifyResultActivity.this, result.getMessage());

                            finish();
                        }
                    }
                });
    }
}

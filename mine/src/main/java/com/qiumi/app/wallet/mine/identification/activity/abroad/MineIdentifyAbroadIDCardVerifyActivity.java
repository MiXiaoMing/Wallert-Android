package com.qiumi.app.wallet.mine.identification.activity.abroad;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;
import com.qiumi.app.wallet.mine.identification.dialog.IDTypeDialog;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 国外 基本身份认证
 */

public class MineIdentifyAbroadIDCardVerifyActivity extends AutoBaseActivity {

    private EditText etFirstName, etMiddleName, etLastName, etIDNum;
    private TextView tvIDType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_identify_abroad_id_card_verify);

        initView();
    }

    private void initView() {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);

        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);

        tvIDType = findViewById(R.id.tvIDType);
        ImageView ivTypeDialog = findViewById(R.id.ivTypeDialog);

        etIDNum = findViewById(R.id.etIDNum);

        TextView tvSubmit = findViewById(R.id.tvSubmit);

        rlyBack.setOnClickListener(clickListener);
        tvIDType.setOnClickListener(clickListener);
        ivTypeDialog.setOnClickListener(clickListener);
        tvSubmit.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.rlyBack) {
                finish();
            } else if (id == R.id.tvIDType || id == R.id.ivTypeDialog) {
                IDTypeDialog dialog = new IDTypeDialog();
                dialog.setOnClickerListener(new IDTypeDialog.OnClickListener() {
                    @Override
                    public void onClickSure(String value) {
                        tvIDType.setText(Constants.getIDName(value));
                    }
                });
                dialog.show(getFragmentManager(), "id_dialog");
            } else if (id == R.id.tvSubmit) {
                submit();
            }
        }
    };

    /**
     * 提交 国外 基本信息认证
     */
    private void submit() {
        new MineDataManager()
                .abroadVerifyIDCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        if (ActivityUtil.isAvailable(MineIdentifyAbroadIDCardVerifyActivity.this)) {
                            AFToast.showShort(MineIdentifyAbroadIDCardVerifyActivity.this, R.string.mine_identification_upload_id_card_result_success);
                            finish();
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EmptyHttpResult result) {
                        if (!result.getStatus().equals("0")) {
                            Logger.getLogger().e("提交基本信息认证，msgCode：" + result.getMessage() + "/n");
                            if (ActivityUtil.isAvailable(MineIdentifyAbroadIDCardVerifyActivity.this)) {
                                if (TextUtils.isEmpty(result.getMessage()))
                                    AFToast.showShort(MineIdentifyAbroadIDCardVerifyActivity.this, result.getMessage());
                                else
                                    AFToast.showShort(MineIdentifyAbroadIDCardVerifyActivity.this, R.string.netword_fail);
                            }
                            return;
                        }


                    }
                });
    }
}

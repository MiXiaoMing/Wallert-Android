package com.qiumi.app.wallet.mine.identification.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.security.rp.RPSDK;
import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.aoglrithm.Random;
import com.appframe.utils.app.AppRuntimeUtil;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.api.input.CreateBioSessionBody;
import com.qiumi.app.wallet.mine.api.input.Kyc2SubmitBody;
import com.qiumi.app.wallet.mine.api.input.SecurityLevelBody;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;
import com.qiumi.app.wallet.mine.api.output.CreateBioSessionEntity;
import com.qiumi.app.wallet.mine.api.output.SecurityLevelEntity;
import com.qiumi.app.wallet.mine.identification.activity.abroad.MineIdentifyAbroadHighLevelVerifyActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 身份认证 主页面
 */

public class MineIdentifyMainActivity extends AutoBaseActivity {
    private String userNation = "", securityLevel = "";
    private TextView tvBase, tvMore;
    private String bioToken = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_identification_main);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getLevel();
    }

    private void initView() {
        findViewById(R.id.rlyBack).setOnClickListener(clickListener);
        findViewById(R.id.ll_base_identification).setOnClickListener(clickListener);
        findViewById(R.id.ll_high_identification).setOnClickListener(clickListener);

        tvBase = findViewById(R.id.tvBase);
        tvMore = findViewById(R.id.tvMore);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.rlyBack) {
                finish();
                return;
            }

            if (TextUtils.isEmpty(userNation) || TextUtils.isEmpty(securityLevel)) {
                AFToast.showShort(MineIdentifyMainActivity.this, R.string.netword_fail);
                return;
            }

            if (id == R.id.ll_base_identification) {
                if (securityLevel.equals("1") || securityLevel.equals("2")) {
                    AFToast.showShort(MineIdentifyMainActivity.this, "已认证");
                    return;
                }

                if (userNation.equals("1")) {
                    // 打开国内基本实名认证页。
                    startActivity(new Intent(MineIdentifyMainActivity.this, MineIdentifyIDCardVerifyActivity.class));
                } else {
                    // 打开国家选择页。
                    Intent intent = new Intent(MineIdentifyMainActivity.this, MineIdentifySelectCountryActivity.class);
                    startActivity(intent);
                }
            } else if (id == R.id.ll_high_identification) {
                if (securityLevel.equals("2")) {
                    AFToast.showShort(MineIdentifyMainActivity.this, "已认证");
                    return;
                }

                if (userNation.equals("1")) {
                    createBioSession();
                } else if (userNation.equals("2")) {
                    // 打开国外高级信息页。
                    Intent intent = new Intent(MineIdentifyMainActivity.this, MineIdentifyAbroadHighLevelVerifyActivity.class);
                    startActivity(intent);
                } else {
                    Logger.getLogger().e("不识别的用户类型:" + userNation);
                }
            }
        }
    };

    private void initData(SecurityLevelEntity data) {
        userNation = data.nation;
        securityLevel = data.securityLevel;

        if (securityLevel.equals("1")) {
            tvBase.setText("已认证");
            tvMore.setText("去认证");
        } else if (securityLevel.equals("2")) {
            tvBase.setText("已认证");
            tvMore.setText("已认证");
        } else {
            tvBase.setText("去认证");
            tvMore.setText("去认证");
        }
    }

    // userNation： 1 为大陆用户 2为国际或港澳台用户
    // KYC1：基本认证 0:未认证 1:已认证 2:待审核 3:审核未通过，请重新提交
    // KYC2：高级认证 0:未认证 1:已认证 2:待审核 3:审核未通过，请重新提交
    // payPass 1为已设置支付密码 0位未设置支付密码
    private void getLevel() {
        SecurityLevelBody body = new SecurityLevelBody();
        body.coinId = "2";

        new MineDataManager()
                .securityLevel(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<SecurityLevelEntity>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(SecurityLevelEntity result) {
                        initData(result.getData());
                    }
                });
    }

    private void createBioSession() {
        CreateBioSessionBody body = new CreateBioSessionBody();
        body.timestamp = String.valueOf(System.currentTimeMillis());
        body.nonce = Random.getUUID();

        Map<String, String> signParams = new HashMap<>();
        signParams.put("timestamp", body.timestamp);
        signParams.put("nonce", body.nonce);
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new MineDataManager()
                .createBioSession(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<CreateBioSessionEntity>() {

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(CreateBioSessionEntity result) {
                        bioToken = result.getData().token;
                        startVerify(bioToken);
                    }
                });
    }

    private void startVerify(String token) {
        RPSDK.initialize(AppRuntimeUtil.getInstance().getCurrentActivity());

        RPSDK.start(token, AppRuntimeUtil.getInstance().getCurrentActivity(), new RPSDK.RPCompletedListener() {
            @Override
            public void onAuditResult(RPSDK.AUDIT audit, String code) {

                AFToast.showShort(AppRuntimeUtil.getInstance().getCurrentActivity(), audit + ">>>" + code);

                if (audit == RPSDK.AUDIT.AUDIT_PASS) {
                    Logger.getLogger().d("RPSDK.AUDIT.AUDIT_PASS");
                    // 认证通过。建议接入方调用实人认证服务端接口DescribeVerifyResult来获取最终的认证状态，并以此为准进行业务上的判断和处理

                    kyc2Submit();
                } else if(audit == RPSDK.AUDIT.AUDIT_FAIL) {
                    Logger.getLogger().e("RPSDK.AUDIT.AUDIT_FAIL");
                    AFToast.showShort(MineIdentifyMainActivity.this, "认证失败，请重新认证");
                    // 认证不通过。建议接入方调用实人认证服务端接口DescribeVerifyResult来获取最终的认证状态，并以此为准进行业务上的判断和处理
                } else if(audit == RPSDK.AUDIT.AUDIT_NOT) {
                    Logger.getLogger().e("RPSDK.AUDIT.AUDIT_NOT");
                    // 未认证，具体原因可通过code来区分（code取值参见下方表格），通常是用户主动退出或者姓名身份证号实名校验不匹配等原因，导致未完成认证流程
                }
            }
        });
    }

    private void kyc2Submit() {
        Kyc2SubmitBody body = new Kyc2SubmitBody();
        body.bioVerifyToken = bioToken;

        new MineDataManager()
                .kyc2Submit(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        if (ActivityUtil.isAvailable(MineIdentifyMainActivity.this)) {
                            AFToast.showShort(MineIdentifyMainActivity.this, "认证成功");
                            getLevel();
                        }
                    }
                });
    }
}

package com.qiumi.app.wallet.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseActivity;

/**
 * Created by ThinkPad on 2019/9/11.
 * 国外高级用户找回密码 重置密码需要引入人工审核机制
 */


@Route(path = ARouterPaths.LOGIN_MANUAL_AUDIT_ACTIVITY)
public class ManualAuditActivity extends AutoBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_manual_audit);

        initViews();
    }

    private void initViews() {
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        TextView tvSure = findViewById(R.id.tv_sure);
        TextView tvCancel = findViewById(R.id.tv_cancel);

        tvSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.tv_sure) {

        } else if (v.getId() == R.id.tv_cancel) {
            finish();
        }
    }

}

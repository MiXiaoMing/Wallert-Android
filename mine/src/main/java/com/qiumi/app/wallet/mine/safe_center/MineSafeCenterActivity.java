package com.qiumi.app.wallet.mine.safe_center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.login_password.MineChangeLoginPasswordActivity;
import com.qiumi.app.wallet.mine.pay_password.MineSetPayPasswordActivity;

/**
 * 安全中心页。
 */

public class MineSafeCenterActivity extends AutoBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_safe_center);

        initView();
    }

    private void initView() {
        findViewById(R.id.ll_set_password).setOnClickListener(clickListener);
        findViewById(R.id.ll_change_login_password).setOnClickListener(clickListener);

        findViewById(R.id.rlyBack).setOnClickListener(clickListener);
        TextView tvTelephone = findViewById(R.id.tvTelephone);

        tvTelephone.setText(UserUtil.getTelephone());
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.ll_set_password) {
                startActivity(new Intent(MineSafeCenterActivity.this, MineSetPayPasswordActivity.class));
            } else if (id == R.id.ll_change_login_password) {
                startActivity(new Intent(MineSafeCenterActivity.this, MineChangeLoginPasswordActivity.class));
            } else if (id == R.id.rlyBack) {
                finish();
            }
        }
    };
}

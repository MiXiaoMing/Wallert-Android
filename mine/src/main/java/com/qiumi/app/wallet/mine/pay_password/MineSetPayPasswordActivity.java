package com.qiumi.app.wallet.mine.pay_password;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.wallet.mine.R;

/**
 * 设置支付密码页。（第一步）
 */

public class MineSetPayPasswordActivity extends AutoBaseActivity {

    // 步骤
    private int step = 0;
    private String password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_set_pay_password);

        findViewById(R.id.rlyBack).setOnClickListener(clickListener);

        //初始化控件
        final MinePayPasswordView pwdView = findViewById(R.id.pwd_view);

        // 添加回调接口
        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                if (step == 0) {
                    password = pwdView.getStrPassword();
                    pwdView.clearPassword();

                    step = 1;
                } else if (step == 1){
                    if (password.equals(pwdView.getStrPassword())) {
                        // 打开获取验证码页。
                        Intent intent = new Intent(MineSetPayPasswordActivity.this, MineSetPayPasswordVerificationActivity.class);
                        intent.putExtra("password", password);
                        startActivity(intent);

                        finish();
                    } else {
                        pwdView.showPasswordError(true);
                        // 重新输入
                        step = 0;
                        pwdView.clearPassword();
                        password = "";
                    }
                }
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.rlyBack) {
                finish();
            }
        }
    };
}

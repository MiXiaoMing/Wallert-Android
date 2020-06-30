package com.qiumi.app.wallet.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.ClipboardUtil;
import com.qiumi.app.support.AutoBaseActivity;

/**
 * 社群
 */

public class CommunityActivity extends AutoBaseActivity {
    private TextView tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_community);

        initView();
    }

    private void initView() {
        findViewById(R.id.rlyBack).setOnClickListener(clickListener);
        findViewById(R.id.tvCopy).setOnClickListener(clickListener);

        tvName = findViewById(R.id.tvName);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.rlyBack) {
                finish();
            } else if (id == R.id.tvCopy) {
                if (ClipboardUtil.copy(CommunityActivity.this, tvName.getText().toString().trim())) {
                    AFToast.showShort(CommunityActivity.this, R.string.copy_success);
                }
            }
        }
    };
}

package com.qiumi.app.wallet.mine.identification.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.component.BaseViewInterface;
import com.qiumi.app.wallet.mine.R;

/**
 * 高级身份认证-face++视频认证。todo 实现face++功能
 *
 * @author jiangkun
 * @date 2019/9/12
 */

public class MineIdentifyFaceVerifyActivity extends AutoBaseActivity implements BaseViewInterface {

    public static void actionStart(Context context) {
        try {
            Intent intent = new Intent(context, MineIdentifyFaceVerifyActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mine_activity_identification_face_verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(Context context) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void setDataToView() {

    }
}

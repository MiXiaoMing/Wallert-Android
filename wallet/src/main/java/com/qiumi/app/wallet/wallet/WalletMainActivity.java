package com.qiumi.app.wallet.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.android.arouter.launcher.ARouter;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.AutoBaseFragment;

/**
 * @author jiangkun
 * @date 2019/8/21
 */

public class WalletMainActivity extends AutoBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AutoBaseFragment walletFragment = new WalletFragment();
        transaction.add(R.id.fl_container, walletFragment).commit();
    }
}

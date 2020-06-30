package com.qiumi.app.wallet.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.android.arouter.launcher.ARouter;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.AutoBaseFragment;

/**
 * @author jiangkun
 * @date 2019/8/22
 */

public class FindMainActivity extends AutoBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AutoBaseFragment findFragment = new FindFragment();
        transaction.add(R.id.fl_container, findFragment).commit();
    }
}

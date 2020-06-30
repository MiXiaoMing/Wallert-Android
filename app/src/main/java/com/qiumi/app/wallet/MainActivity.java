package com.qiumi.app.wallet;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.appframe.library.component.notify.AFToast;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.AutoBaseFragment;
import com.qiumi.app.support.runtimepermissions.PermissionsManager;
import com.qiumi.app.support.runtimepermissions.PermissionsResultAction;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.find.FindFragment;
import com.qiumi.app.wallet.login.LoginActivity;
import com.qiumi.app.wallet.mine.MineFragment;
import com.qiumi.app.wallet.wallet.WalletFragment;

@Route(path = ARouterPaths.MAIN_ACTIVITY)
public class MainActivity extends AutoBaseActivity {

    private TextView tvWallet, tvFind, tvMine;
    private ImageView ivWallet, ivFind, ivMine;
    private FragmentManager fragmentManager;
    private AutoBaseFragment walletFragment, findFragment, mineFragment, currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout llyWallet = findViewById(R.id.llyWallet);
        tvWallet = findViewById(R.id.tvWallet);
        ivWallet = findViewById(R.id.ivWallet);

        LinearLayout llyFind = findViewById(R.id.llyFind);
        tvFind = findViewById(R.id.tvFind);
        ivFind = findViewById(R.id.ivFind);

        LinearLayout llyMine = findViewById(R.id.llyMine);
        tvMine = findViewById(R.id.tvMine);
        ivMine = findViewById(R.id.ivMine);


        llyWallet.setOnClickListener(clickListener);
        llyFind.setOnClickListener(clickListener);
        llyMine.setOnClickListener(clickListener);


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        walletFragment = new WalletFragment();
        fragmentTransaction.add(R.id.flyContainer, walletFragment).commit();
        currentFragment = walletFragment;

        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!UserUtil.isLogin() && walletFragment != null && currentFragment != walletFragment) {
            changeToWallet();
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyWallet:
                    if (currentFragment != walletFragment) {
                        changeToWallet();
                    }
                    break;

                case R.id.llyFind:
                    if (currentFragment != findFragment) {
                        changeToFind();
                    }
                    break;

                case R.id.llyMine:
                    if (!UserUtil.isLogin()) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        return;
                    }
                    if (currentFragment != mineFragment) {
                        changeToMine();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    private long clickTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - clickTime < 2000) {
                finish();
            } else {
                AFToast.showShort(MainActivity.this, "再点一次将退出程序");
                clickTime = secondTime;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeToWallet() {
        ivWallet.setImageResource(R.drawable.icon_wallet_clicked);
        tvWallet.setTextColor(getResources().getColor(R.color.blue));
        ivFind.setImageResource(R.drawable.icon_find);
        tvFind.setTextColor(getResources().getColor(R.color.black_1A));
        ivMine.setImageResource(R.drawable.icon_mine);
        tvMine.setTextColor(getResources().getColor(R.color.black_1A));

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(currentFragment).show(walletFragment).commit();
        currentFragment = walletFragment;
    }

    private void changeToFind() {
        ivWallet.setImageResource(R.drawable.icon_wallet);
        tvWallet.setTextColor(getResources().getColor(R.color.black_1A));
        ivFind.setImageResource(R.drawable.icon_find_clicked);
        tvFind.setTextColor(getResources().getColor(R.color.blue));
        ivMine.setImageResource(R.drawable.icon_mine);
        tvMine.setTextColor(getResources().getColor(R.color.black_1A));

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (findFragment == null) {
            findFragment = new FindFragment();
            fragmentTransaction.hide(currentFragment).add(R.id.flyContainer, findFragment).commit();
        } else {
            fragmentTransaction.hide(currentFragment).show(findFragment).commit();
        }
        currentFragment = findFragment;
    }

    private void changeToMine() {
        ivWallet.setImageResource(R.drawable.icon_wallet);
        tvWallet.setTextColor(getResources().getColor(R.color.black_1A));
        ivFind.setImageResource(R.drawable.icon_find);
        tvFind.setTextColor(getResources().getColor(R.color.black_1A));
        ivMine.setImageResource(R.drawable.icon_mine_clicked);
        tvMine.setTextColor(getResources().getColor(R.color.blue));

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (mineFragment == null) {
            mineFragment = new MineFragment();
            fragmentTransaction.hide(currentFragment).add(R.id.flyContainer, mineFragment).commit();
        } else {
            fragmentTransaction.hide(currentFragment).show(mineFragment).commit();
        }
        currentFragment = mineFragment;
    }
}

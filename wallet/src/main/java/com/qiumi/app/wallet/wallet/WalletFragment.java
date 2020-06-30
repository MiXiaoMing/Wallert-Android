package com.qiumi.app.wallet.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.library.component.image.ImageLoader;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseFragment;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.utils.AppUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.wallet.adapter.AssetAdapter;
import com.qiumi.app.wallet.wallet.api.iterfaces.WalletDataManager;
import com.qiumi.app.wallet.wallet.api.output.Asset;
import com.qiumi.app.wallet.wallet.api.output.AssetsEntity;
import com.qiumi.app.wallet.wallet.api.output.UserInfoEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 首页面
 */
public class WalletFragment extends AutoBaseFragment {
    private View view;

    private LinearLayout llyUser;
    private ImageView ivAvatar;
    private TextView tvName, tvLogin;

    private TextView tvAssetUSDT, tvAsset;

    private ListView lvAssetList;
    private AssetAdapter assetAdapter;

    private LinearLayout llyUnLogin;

    // 保存数据，为了传递
    private List<Asset> assets = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_wallet, null);
            init(view);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    private void init(View view) {
        llyUser = view.findViewById(R.id.llyUser);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvLogin = view.findViewById(R.id.tvLogin);

        tvAssetUSDT = view.findViewById(R.id.tvAssetUSDT);
        tvAsset = view.findViewById(R.id.tvAsset);
        LinearLayout llyBalanceRecord = view.findViewById(R.id.llyAssetRecord);

        lvAssetList = view.findViewById(R.id.lvAssetList);
        llyUnLogin = view.findViewById(R.id.llyUnLogin);
        LinearLayout itemBalance = view.findViewById(R.id.itemAsset);

        // 显示资产列表
        assetAdapter = new AssetAdapter(this.getActivity());
        lvAssetList.setAdapter(assetAdapter);
        lvAssetList.setOnItemClickListener(itemClickListener);

        tvLogin.setOnClickListener(clickListener);
        llyBalanceRecord.setOnClickListener(clickListener);
        itemBalance.setOnClickListener(clickListener);
    }

    private void changeStatus() {
        if (UserUtil.isLogin()) {
            initUserInfo();

            tvLogin.setVisibility(View.GONE);

            tvAsset.setVisibility(View.VISIBLE);

            // 点击区域变化
            llyUser.setClickable(false);
            tvName.setClickable(true);
            tvLogin.setClickable(true);
            tvName.setOnClickListener(clickListener);
            tvLogin.setOnClickListener(clickListener);

            tvAssetUSDT.setClickable(false);

            lvAssetList.setVisibility(View.VISIBLE);
            llyUnLogin.setVisibility(View.GONE);

            // 获取最新数据
            getUserInfo();
            getAssets();
        } else {
            ImageLoader.circle(WalletFragment.this.getActivity(), R.drawable.icon_avatar, ivAvatar);
            tvName.setText(getResources().getString(R.string.unlogin));
            tvLogin.setVisibility(View.VISIBLE);

            tvAsset.setVisibility(View.GONE);

            // 点击区域变化
            llyUser.setClickable(true);
            tvName.setClickable(false);
            tvLogin.setClickable(false);
            llyUser.setOnClickListener(clickListener);


            tvAssetUSDT.setText("-----");
            tvAssetUSDT.setClickable(true);
            tvAssetUSDT.setOnClickListener(clickListener);

            lvAssetList.setVisibility(View.GONE);
            llyUnLogin.setVisibility(View.VISIBLE);
        }
    }

    private void initUserInfo() {
        ImageLoader.circle(WalletFragment.this.getActivity(), UserUtil.getAvatar(), ivAvatar);
        if (TextUtils.isEmpty(UserUtil.getTelephone())) {
            tvName.setText(getResources().getString(R.string.welcome) + "，" + UserUtil.getUserID());
        } else {
            tvName.setText(getResources().getString(R.string.welcome) + "，" + UserUtil.getTelephone());
        }
    }

    private void initAssetsData(AssetsEntity entity) {
        if (entity == null) {
            return;
        }

        tvAssetUSDT.setText(entity.price);
        if (AppUtil.getCurrencyType().equalsIgnoreCase(Constants.currency_rmb)) {
            tvAsset.setText("≈ ￥" + entity.priceTans);
        } else {
            tvAsset.setText("≈ $" + entity.price);
        }

        UserUtil.setTotalUSDT(entity.price);

        List<Asset> assets = entity.asserts;
        if (assets == null || assets.size() <= 0) {
            return;
        }

        assetAdapter.addAll(assets);
        this.assets.clear();
        this.assets.addAll(assets);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeStatus();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.llyUser) {
                goLoginActivity();
            } else if (viewId == R.id.tvAssetUSDT) {
                goLoginActivity();
            } else if (viewId == R.id.itemAsset) {
                goLoginActivity();
            } else if (viewId == R.id.tvName || viewId == R.id.ivAvatar) {
                // TODO: 2019/8/19 跳转到个人详情页面
                AFToast.showShort(WalletFragment.this.getActivity(), R.string.waiting);
            } else if (viewId == R.id.llyAssetRecord) {
                if (UserUtil.isLogin()) {
                    Intent intent = new Intent(WalletFragment.this.getActivity(), AssetRecordActivity.class);
                    intent.putExtra("assets", (Serializable) WalletFragment.this.assets);
                    intent.putExtra("operTypes", (Serializable) Constants.operTypes);
                    startActivity(intent);
                } else {
                    goLoginActivity();
                }
            }
        }
    };

    private ListView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(WalletFragment.this.getActivity(), CoinDetailActivity.class);
            intent.putExtra("coin", assets.get(position).currencyId);
            intent.putExtra("coinName", assets.get(position).name);
            startActivity(intent);
        }
    };

    // 跳转到登录页面
    private void goLoginActivity() {
        ARouter.getInstance().build(ARouterPaths.LOGIN_ACTIVITY).navigation();
    }

    /******* 网络  *******/

    /**
     * 获取用户基本信息
     */
    private void getUserInfo() {
        new WalletDataManager()
                .userInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<UserInfoEntity>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(UserInfoEntity result) {
                        if (result.getData() == null) {
                            Logger.getLogger().e("获取用户基本信息 result为空");
                            return;
                        }

                        UserUtil.setTelephone(result.getData().mobileSimple);
                        UserUtil.setAvatar(result.getData().headicon);

                        initUserInfo();
                    }
                });
    }

    /**
     * 获取资产信息
     */
    private void getAssets() {
        new WalletDataManager()
                .assets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<AssetsEntity>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(AssetsEntity result) {
                        initAssetsData(result.getData());
                    }
                });
    }
}

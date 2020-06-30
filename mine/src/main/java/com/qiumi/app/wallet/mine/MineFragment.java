package com.qiumi.app.wallet.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appframe.library.component.notify.AFToast;
import com.qiumi.app.support.AutoBaseFragment;
import com.qiumi.app.wallet.mine.identification.activity.MineIdentifyMainActivity;
import com.qiumi.app.wallet.mine.safe_center.MineSafeCenterActivity;
import com.qiumi.app.wallet.mine.settings.MineSettingsActivity;

/**
 * 首页--我的。
 */
public class MineFragment extends AutoBaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_mine, container, false);

        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        initView(mView);

        return mView;
    }

    public void initView(View view) {
        view.findViewById(R.id.ll_safe_center).setOnClickListener(clickListener);
        view.findViewById(R.id.ll_identification).setOnClickListener(clickListener);
        view.findViewById(R.id.iv_settings).setOnClickListener(clickListener);
        view.findViewById(R.id.llyFriends).setOnClickListener(clickListener);
        view.findViewById(R.id.llyHelp).setOnClickListener(clickListener);
        view.findViewById(R.id.llyCommunity).setOnClickListener(clickListener);
    }

    public View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.ll_safe_center) {
                // 进入安全中心页。
                startActivity(new Intent(getContext(), MineSafeCenterActivity.class));
            } else if (id == R.id.ll_identification) {
                // 进入身份认证页
                startActivity(new Intent(getActivity(), MineIdentifyMainActivity.class));
            } else if (id == R.id.llyFriends) {
                AFToast.showShort(getActivity(), R.string.waiting);
            } else if (id == R.id.iv_settings) {
                // 进入系统设置页面
                startActivity(new Intent(getActivity(), MineSettingsActivity.class));
            } else if (id == R.id.llyHelp) {
                AFToast.showShort(getActivity(), R.string.waiting);
            } else if (id == R.id.llyCommunity) {
                startActivity(new Intent(getActivity(), CommunityActivity.class));
            }
        }
    };
}

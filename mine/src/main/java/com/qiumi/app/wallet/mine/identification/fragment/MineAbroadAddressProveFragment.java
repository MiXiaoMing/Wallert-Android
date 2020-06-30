package com.qiumi.app.wallet.mine.identification.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiumi.app.support.AutoBaseFragment;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.identification.activity.abroad.MineIdentifyAbroadHighLevelVerifyActivity;

import org.w3c.dom.Text;

/**
 * 海外-高级信息认证-住址证明。
 */

public class MineAbroadAddressProveFragment extends AutoBaseFragment {
    private ImageView ivAddress;
    private String address;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.mine_fragment_abroad_address_prove, container, false);
        initView();
        return mRootView;
    }

    public void initView() {
        FrameLayout flyAddress = mRootView.findViewById(R.id.flyAddress);
        ivAddress = mRootView.findViewById(R.id.ivAddress);

        TextView mTvBackStep = mRootView.findViewById(R.id.tv_back_step);
        TextView tvSubmit = mRootView.findViewById(R.id.tvSubmit);

        flyAddress.setOnClickListener(clickListener);
        mTvBackStep.setOnClickListener(clickListener);
        tvSubmit.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tv_back_step) {
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).updateProcess(MineIdentifyAbroadHighLevelVerifyActivity.TAG_ADDRESS_INFO);
                }
            } else if (id == R.id.flyAddress) {
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).showPictureChooseDialog(MineAbroadAddressProveFragment.this);
                }
            } else if (id == R.id.tvSubmit) {
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).updateAddressProve(address);
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).submit();
                }
            }
        }
    };

    @Override
    public void result(String path) {
        this.address = path;
        Glide.with(this).load(path).into(ivAddress);
    }
}

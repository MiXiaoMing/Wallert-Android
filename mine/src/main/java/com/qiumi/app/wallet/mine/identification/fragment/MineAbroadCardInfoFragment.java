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

/**
 * 海外-高级信息认证-证件信息。
 */

public class MineAbroadCardInfoFragment extends AutoBaseFragment {
    private ImageView ivFront, ivBack;

    private String front, back;
    private int type = 0; //1:正面 2：背面

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.mine_fragment_abroad_card_info, container, false);
        initView();
        return mRootView;
    }

    public void initView() {
        TextView mTvBackStep = mRootView.findViewById(R.id.tv_back_step);
        TextView mTvNextStep = mRootView.findViewById(R.id.tv_next_step);

        FrameLayout flyFront = mRootView.findViewById(R.id.flyFront);
        FrameLayout flyBack = mRootView.findViewById(R.id.flyBack);
        ivBack = mRootView.findViewById(R.id.ivBack);
        ivFront = mRootView.findViewById(R.id.ivFront);

        mTvBackStep.setOnClickListener(clickListener);
        mTvNextStep.setOnClickListener(clickListener);
        flyFront.setOnClickListener(clickListener);
        flyBack.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tv_back_step) {
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).updateProcess(MineIdentifyAbroadHighLevelVerifyActivity.TAG_BASE_INFO);
                }
            } else if (id == R.id.tv_next_step) {
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).updateCardInfo(front, back);
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).updateProcess(MineIdentifyAbroadHighLevelVerifyActivity.TAG_ADDRESS_INFO);
                }
            } else if (id == R.id.flyFront) {
                type = 1;
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).showPictureChooseDialog(MineAbroadCardInfoFragment.this);
                }
            } else if (id == R.id.flyBack) {
                type = 2;
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).showPictureChooseDialog(MineAbroadCardInfoFragment.this);
                }
            }
        }
    };

    @Override
    public void result(String path) {
        if (type == 1) {
            this.front = path;
            Glide.with(this).load(path).into(ivFront);
        } else {
            this.back = path;
            Glide.with(this).load(path).into(ivBack);
        }
    }

}
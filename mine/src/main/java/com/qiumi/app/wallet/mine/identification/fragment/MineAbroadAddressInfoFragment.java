package com.qiumi.app.wallet.mine.identification.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.qiumi.app.support.AutoBaseFragment;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.identification.activity.abroad.MineIdentifyAbroadHighLevelVerifyActivity;

/**
 * 海外-高级信息认证-住址信息。
 */

public class MineAbroadAddressInfoFragment extends AutoBaseFragment {

    private EditText etAddress, etAddressDetail, etCity, etCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.mine_fragment_abroad_address_info, container, false);
        initView();
        return mRootView;
    }

    public void initView() {
        TextView mTvBackStep = mRootView.findViewById(R.id.tv_back_step);
        TextView mTvNextStep = mRootView.findViewById(R.id.tv_next_step);

        etAddress = mRootView.findViewById(R.id.etAddress);
        etAddressDetail = mRootView.findViewById(R.id.etAddressDetail);
        etCity = mRootView.findViewById(R.id.etCity);
        etCode = mRootView.findViewById(R.id.etCode);

        mTvBackStep.setOnClickListener(clickListener);
        mTvNextStep.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.tv_back_step) {
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).updateProcess(MineIdentifyAbroadHighLevelVerifyActivity.TAG_CARD_INFO);
                }
            } else if (id == R.id.tv_next_step) {
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).updateAddress(
                            etAddress.getText().toString().trim(),
                            etAddressDetail.getText().toString().trim(),
                            etCity.getText().toString().trim(),
                            etCode.getText().toString().trim()
                    );
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).updateProcess(MineIdentifyAbroadHighLevelVerifyActivity.TAG_ADDRESS_PROVE);
                }
            }
        }
    };
}

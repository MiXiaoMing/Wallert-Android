package com.qiumi.app.wallet.mine.identification.fragment;

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
 * 海外-高级信息认证-基本信息。
 */

public class MineAbroadBaseInfoFragment extends AutoBaseFragment {
    /**
     * 下一步按钮
     */
    private TextView mTvNextStep;
    private EditText etCountry, etFirstName, etMiddleName, etLastName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.mine_fragment_abroad_base_info, container, false);
        initView();
        return mRootView;
    }

    public void initView() {
        mTvNextStep = mRootView.findViewById(R.id.tv_next_step);

        etCountry = mRootView.findViewById(R.id.etCountry);
        etFirstName = mRootView.findViewById(R.id.etFirstName);
        etMiddleName = mRootView.findViewById(R.id.etMiddleName);
        etLastName = mRootView.findViewById(R.id.etLastName);

        mTvNextStep.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_next_step) {
                if (mActivity instanceof MineIdentifyAbroadHighLevelVerifyActivity) {
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity)
                            .updateBaseInfo(etCountry.getText().toString().trim(), etFirstName.getText().toString().trim(),
                                    etMiddleName.getText().toString().trim(), etLastName.getText().toString().trim());
                    ((MineIdentifyAbroadHighLevelVerifyActivity) mActivity).updateProcess(MineIdentifyAbroadHighLevelVerifyActivity.TAG_CARD_INFO);
                }
            }
        }
    };
}

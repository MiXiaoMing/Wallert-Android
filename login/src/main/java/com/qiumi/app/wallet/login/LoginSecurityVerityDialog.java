package com.qiumi.app.wallet.login;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.appframe.library.component.notify.AFToast;
import com.qiumi.app.support.ARouterPaths;
import com.qiumi.app.support.AutoBaseDialogFragment;
import com.qiumi.app.wallet.login.utils.SecurityVerityType;
import com.zhy.autolayout.utils.AutoUtils;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

/**
 * Created by ThinkPad on 2019/9/10.
 */

public class LoginSecurityVerityDialog extends AutoBaseDialogFragment implements View.OnClickListener {

    private static final String TAG = "LoginSecurityVerityDial";
    private static final String SECURITY_TYPE = "TYPE";

    private static final int WIDTH = 305;
    private static final int CHINA_BASIC_HEIHGT = 192;
    private static final int CHINA_ADVANCE_HEIHGT = 334;
    private static final int ABROAD_BASIC_HEIHGT = 265;
    private static final int ABROAD_ADVANCE_HEIHGT = 397;

    private SecurityVerityType mCurType = SecurityVerityType.CHINA_BASIC;

    public static LoginSecurityVerityDialog getInstance(SecurityVerityType securityVerityType) {
        LoginSecurityVerityDialog dialog = new LoginSecurityVerityDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SECURITY_TYPE, securityVerityType);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.login_dialog_security_verity;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = 0;
            if (mCurType == SecurityVerityType.CHINA_BASIC) {
                width = CHINA_BASIC_HEIHGT;
            } else if (mCurType == SecurityVerityType.CHINE_ADVANCE) {
                width = CHINA_ADVANCE_HEIHGT;
            }  else if (mCurType == SecurityVerityType.ABROAD_BASIC) {
                width = ABROAD_BASIC_HEIHGT;
            } else if (mCurType == SecurityVerityType.ABROAD_ADVANCE) {
                width = ABROAD_ADVANCE_HEIHGT;
            }
            getDialog().getWindow().setLayout(AutoUtils.getPercentWidthSize(WIDTH), AutoUtils.getPercentHeightSize(width));
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        TextView tvSure = rootView.findViewById(R.id.tv_sure);
        TextView tvCancel = rootView.findViewById(R.id.tv_cancel);

        tvSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        LinearLayout llAbroadBasic = rootView.findViewById(R.id.ll_abroad_basic);
        ImageView ivPicture = rootView.findViewById(R.id.iv_picture);

        NiceSpinner spinnerCredType = rootView.findViewById(R.id.spinner_cred_type);
        final String[] credTypes = getResources().getStringArray(R.array.cred_type);
        spinnerCredType.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                AFToast.showShort(getActivity(), credTypes[position]);
            }
        });


        Bundle arguments = getArguments();
        if (arguments != null) {
            mCurType = (SecurityVerityType) arguments.getSerializable(SECURITY_TYPE);
        }

        if (mCurType == SecurityVerityType.CHINA_BASIC) {
            llAbroadBasic.setVisibility(View.GONE);
            ivPicture.setVisibility(View.GONE);
        } else if (mCurType == SecurityVerityType.CHINE_ADVANCE) {
            llAbroadBasic.setVisibility(View.GONE);
            ivPicture.setVisibility(View.VISIBLE);
        }  else if (mCurType == SecurityVerityType.ABROAD_BASIC) {
            llAbroadBasic.setVisibility(View.VISIBLE);
            ivPicture.setVisibility(View.GONE);
        } else if (mCurType == SecurityVerityType.ABROAD_ADVANCE) {
            llAbroadBasic.setVisibility(View.VISIBLE);
            ivPicture.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_sure) {
            if (mCurType == SecurityVerityType.ABROAD_ADVANCE) {
                ARouter.getInstance().build(ARouterPaths.LOGIN_MANUAL_AUDIT_ACTIVITY).navigation();
            }

        } else if (v.getId() == R.id.tv_cancel) {

        }

    }
}

package com.qiumi.app.wallet.wallet.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.appframe.library.component.notify.AFToast;
import com.qiumi.app.support.AutoBaseDialogFragment;
import com.qiumi.app.wallet.wallet.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by ThinkPad on 2019/9/5.
 */

public class MoneyDialog extends AutoBaseDialogFragment implements View.OnClickListener{

    private EditText etReceiveMoney;
    private String type;

    public void setType(String type) {
        this.type = type;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_money;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(AutoUtils.getPercentWidthSize(305), AutoUtils.getPercentHeightSize(155));
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        TextView tvCancel = rootView.findViewById(R.id.tv_cancel);
        TextView tvSure = rootView.findViewById(R.id.tv_sure);
        etReceiveMoney = rootView.findViewById(R.id.et_receive_money);
        TextView tvType = rootView.findViewById(R.id.tv_type);

        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        tvType.setText(type);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {
            dismiss();
        } else if (v.getId() == R.id.tv_sure) {
            if (TextUtils.isEmpty(etReceiveMoney.getText().toString())) {
                AFToast.showShort(getActivity(), R.string.content_not_null);
            } else {
                dismiss();
                if (mListener != null) {
                    mListener.onClickSure(etReceiveMoney.getText().toString());
                }
            }

        }
    }

    private OnClickListener mListener;
    public interface OnClickListener{
        void onClickSure(String value);
    }

    public void setOnClickerListener(OnClickListener listener) {
        this.mListener = listener;
    }
}

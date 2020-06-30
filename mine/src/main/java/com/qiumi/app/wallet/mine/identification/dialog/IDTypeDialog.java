package com.qiumi.app.wallet.mine.identification.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiumi.app.support.AutoBaseDialogFragment;
import com.qiumi.app.wallet.mine.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 证件类型弹框
 */

public class IDTypeDialog extends AutoBaseDialogFragment {

    private OnClickListener mListener;

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_idtype;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(AutoUtils.getPercentWidthSize(305), AutoUtils.getPercentHeightSize(240));
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        LinearLayout llyDialog = rootView.findViewById(R.id.llyDialog);

        TextView tvIDCard = rootView.findViewById(R.id.tvIDCard);
        TextView tvPassport = rootView.findViewById(R.id.tvPassport);
        TextView tvDrivingLicence = rootView.findViewById(R.id.tvDrivingLicence);

        llyDialog.setOnClickListener(clickListener);
        tvIDCard.setOnClickListener(clickListener);
        tvPassport.setOnClickListener(clickListener);
        tvDrivingLicence.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.llyDialog) {
                dismiss();
            } else if (id == R.id.tvIDCard) {
                dismiss();
                if (mListener != null) {
                    mListener.onClickSure("1");
                }
            } else if (id == R.id.tvPassport) {
                dismiss();
                if (mListener != null) {
                    mListener.onClickSure("2");
                }
            } else if (id == R.id.tvDrivingLicence) {
                dismiss();
                if (mListener != null) {
                    mListener.onClickSure("3");
                }
            }
        }
    };

    public interface OnClickListener {
        void onClickSure(String value);
    }

    public void setOnClickerListener(OnClickListener listener) {
        this.mListener = listener;
    }
}

package com.qiumi.app.wallet.wallet.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.appframe.library.component.notify.AFToast;
import com.qiumi.app.support.component.PasswordView;
import com.qiumi.app.wallet.wallet.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 安全认证，身份证后6位
 */

public class SecurityVerifyDialog extends Dialog implements View.OnClickListener{

    private TextView ftvCancel;
    private TextView ftvSure;
    private PasswordView pvPassword;


    public SecurityVerifyDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.security_verity_dialog, null);
        AutoUtils.auto(rootView);
        setContentView(rootView);
        initViews();

        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = AutoUtils.getPercentWidthSize(305);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = width;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(attributes);

    }


    private void initViews() {
        ftvCancel = findViewById(R.id.ftvCancel);
        ftvSure = findViewById(R.id.ftvSure);
        pvPassword = findViewById(R.id.pvPassword);

        ftvSure.setOnClickListener(this);
        ftvCancel.setOnClickListener(this);
    }


    public String getPassword() {
        return pvPassword.getInputText();
    }


    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.ftvSure) {
            String password = pvPassword.getInputText();
            if (TextUtils.isEmpty(password) || password.length() != 6) {
                AFToast.showShort(getContext(), R.string.idcard_last_6);
                return;
            }

            if (mListener != null) {
                mListener.onPositiveClick(pvPassword.getInputText());
            }
        } else if (v.getId() == R.id.ftvCancel) {

        }
    }


    private OnClickListener mListener;

    public interface OnClickListener{
        void onPositiveClick(String content);

    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }
}

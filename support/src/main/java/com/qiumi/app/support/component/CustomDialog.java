package com.qiumi.app.support.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qiumi.app.support.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by ThinkPad on 2019/9/21.
 */

public class CustomDialog extends Dialog implements View.OnClickListener{

    private TextView ftvTitle;
    private TextView ftvMessage;
    private TextView ftvCancel;
    private TextView ftvSure;
    private View viewDivider;

    private String mTitle;
    private String mMessage;

    private boolean mIsTitle = true;
    private boolean mIsMessage = true;


    private boolean mIsOnlyPositive = false;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
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
        ftvTitle = findViewById(R.id.ftvTitle);
        ftvMessage = findViewById(R.id.ftvMessage);
        ftvCancel = findViewById(R.id.ftvCancel);
        ftvSure = findViewById(R.id.ftvSure);
        viewDivider = findViewById(R.id.viewDivider);

        ftvSure.setOnClickListener(this);
        ftvCancel.setOnClickListener(this);
    }


    public CustomDialog setTitle(String title) {
        mIsTitle = true;
        mTitle = title;
        return this;
    }


    public CustomDialog setMessage(String message) {
        mIsMessage = true;
        mMessage = message;
        return this;
    }

    public CustomDialog setOnlyPositive(boolean isOnlyPositive) {
        mIsOnlyPositive = isOnlyPositive;
        return this;
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    private void refreshView() {
        if (TextUtils.isEmpty(mTitle)) {
            ftvTitle.setVisibility(View.GONE);
        } else {
            ftvTitle.setText(mTitle);
        }

        if (TextUtils.isEmpty(mMessage)) {
            ftvMessage.setVisibility(View.GONE);
        }else {
            ftvMessage.setText(mMessage);
        }

        if (mIsOnlyPositive) {
            viewDivider.setVisibility(View.GONE);
            ftvCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.ftvSure) {
            if (mPositiveListener != null) {
                mPositiveListener.onPositiveClick();
            }
        } else if (v.getId() == R.id.ftvCancel) {
            if (mNegativeListener != null) {
                mNegativeListener.onNegativeClick();
            }
        }
    }

    private OnPositiveClickListener mPositiveListener;

    public interface OnPositiveClickListener{
        void onPositiveClick();
    }

    public void setOnPositiveClickListener(OnPositiveClickListener listener) {
        mPositiveListener = listener;
    }

    private OnNegativeClickListener mNegativeListener;

    public interface OnNegativeClickListener{
        void onNegativeClick();
    }

    public void setOnNegativeClickListener(OnNegativeClickListener listener) {
        mNegativeListener = listener;
    }

//    private OnClickListener mListener;
//
//    public interface OnClickListener{
//        void onPositiveClick();
//
//        void onNegativeClick();
//    }
//
//    public void setOnClickListener(OnClickListener listener) {
//        mListener = listener;
//    }
}

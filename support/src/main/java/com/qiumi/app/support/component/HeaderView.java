package com.qiumi.app.support.component;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiumi.app.support.R;

/**
 * Created by ThinkPad on 2019/9/26.
 */

public class HeaderView extends RelativeLayout implements View.OnClickListener{

    private Context mContext;
    private View mRootView;
    private RelativeLayout rlyBack;
    private TextView tvTitle;
    private TextView tvRightText;
    private ImageView ivBack;

    private String mTitle;
    private String mRightText;

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);
        mTitle = typedArray.getString(R.styleable.HeaderView_headTitle);
        typedArray.recycle();

        if (mTitle != null){
            tvTitle.setText(mTitle);
        }
    }

    private void initView(Context context) {
        mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.header_view, null);
        rlyBack = mRootView.findViewById(R.id.rlyBack);
        ivBack = mRootView.findViewById(R.id.ivBack);
        tvTitle = mRootView.findViewById(R.id.tvTitle);
        tvRightText = mRootView.findViewById(R.id.tvRightText);

        rlyBack.setOnClickListener(this);
        tvRightText.setOnClickListener(this);

        addView(mRootView);


    }

    public void setBackIcon(int id) {
        ivBack.setImageResource(id);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setRightText(String rightText) {
        tvRightText.setText(rightText);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlyBack) {
            if (mContext instanceof Activity) {
                ((Activity)mContext).finish();
            }
            if (mOnBackClickListener != null) {
                mOnBackClickListener.onBackClick();
            }
        } else if (v.getId() == R.id.tvRightText){
            if (mOnRightTextClickListener != null) {
                mOnRightTextClickListener.onRightTextClick();
            }
        }
    }

    private OnBackClickListener mOnBackClickListener;
    public interface OnBackClickListener{
        void onBackClick();
    }
    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        mOnBackClickListener = onBackClickListener;
    }

    private OnRightTextClickListener mOnRightTextClickListener;
    public interface OnRightTextClickListener{
        void onRightTextClick();
    }
    public void setOnRightTextClickListener(OnRightTextClickListener onRightTextClickListener) {
        mOnRightTextClickListener = onRightTextClickListener;
    }
}

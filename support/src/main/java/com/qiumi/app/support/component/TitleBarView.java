package com.qiumi.app.support.component;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiumi.app.support.R;

/**
 * @author jiangkun
 * @date 2019/9/6
 */

public class TitleBarView extends RelativeLayout implements BaseViewInterface {
    /**
     * 注意判空。
     */
    private Context mContext;
    /**
     * 左图标。
     */
    private ImageView mIvLeftButton;
    /**
     * 标题文本。
     */
    private TextView mTvTitle;
    /**
     * 标题文字字符串。
     */
    private String mTitleString;
    /**
     * 背景色。
     */
    private int mBackGroundColorInt;
    /**
     * 左箭头图标。
     */
    private int mLeftIconDrawableInt;
    /**
     * 标题是否居中flag。
     */
    private boolean mIsTitleGravityCenter;
    /**
     * 标题文本颜色。
     */
    private int mTitleTextColorInt;
    /**
     * 点击mIvLeftButton的回调。
     */
    private OnClickListener mOnLeftButtonClickListener;

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initData();
        initView(context);
        initListener();
        setDataToView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
            // 背景色默认红色。
            mBackGroundColorInt = typedArray.getColor(R.styleable.TitleBarView_backgroundColor, getResources().getColor(R.color.header_view_bg_color));
            // 左图标。
            mLeftIconDrawableInt = typedArray.getResourceId(R.styleable.TitleBarView_leftIconDrawable, R.drawable.icon_back);
            // 标题默认居中。
            mIsTitleGravityCenter = typedArray.getBoolean(R.styleable.TitleBarView_isTitleGravityCenter, true);
            // 标题文字字符串。
            mTitleString = typedArray.getString(R.styleable.TitleBarView_title);
            // 标题文字颜色，默认白色。
            mTitleTextColorInt = typedArray.getColor(R.styleable.TitleBarView_titleTextColor, getResources().getColor(R.color.white));
            // 防止内存问题。
            typedArray.recycle();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(Context context) {
        try {
            mContext = context;
            LayoutInflater.from(context).inflate(R.layout.title_bar_view, this, true);
            mIvLeftButton = findViewById(R.id.iv_left_button);
            mTvTitle = findViewById(R.id.tv_title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        try {
            mIvLeftButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如果mOnLeftButtonClickListener != null，交由回调执行。
                    if (mOnLeftButtonClickListener != null) {
                        mOnLeftButtonClickListener.onClick(mIvLeftButton);
                    } else {
                        // mContext always not null。
                        if (mContext instanceof Activity && !((Activity) mContext).isFinishing() && !((Activity) mContext).isDestroyed()) {
                            // 默认页面finish。
                            ((Activity) mContext).finish();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataToView() {
        try {
            // 设置背景色。
            setBackgroundColor(mBackGroundColorInt);
            // 设置左图标。
            mIvLeftButton.setImageDrawable(getResources().getDrawable(mLeftIconDrawableInt));
            // 设置标题位置。
            if (mIsTitleGravityCenter) {
                // 标题居中。
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                mTvTitle.setLayoutParams(layoutParams);
            }
            // 设置标题文本颜色。
            mTvTitle.setTextColor(mTitleTextColorInt);
            // 设置标题文本。
            mTvTitle.setText(mTitleString);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题文字。
     *
     * @param title 标题文字。
     */
    public void setTitle(String title) {
        try {
            mTvTitle.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 左图标显示关闭样式。
     */
    public void showIvLeftClose() {
        try {
            mIvLeftButton.setImageResource(R.drawable.icon_close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 左图标显示返回样式。
     */
    public void showIvLeftBack() {
        try {
            mIvLeftButton.setImageResource(R.drawable.icon_back);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置点击mIvLeftButton的回调。
     *
     * @param onClickListener onClickListener
     */
    public void setOnLeftButtonClickListener(OnClickListener onClickListener) {
        this.mOnLeftButtonClickListener = onClickListener;
    }
}

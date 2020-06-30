package com.qiumi.app.support.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.qiumi.app.support.R;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * Created by ThinkPad on 2019/9/9.
 */

public class PasswordView extends EditText {

    /**
     * 密码长度 默认6位
     */
    private int mPasswordLength = 6;

    /**
     * 密码中间原点显示半径
     */
    private int mPasswordRadius = 20;

    /**
     * 密码中间原点颜色
     */
    private int mPasswordColor = 0xff000000;

    /**
     * 密码边框颜色
     */
    private int mPasswordBorderColor = 0x66000000;

    /**
     * 密码边框是否显示整个边框还是只显示底部
     */
    private boolean mPasswordBorderAll = false;

    /**
     * 是否边框是正方形，如果是则间距不生效
     */
    private boolean mPasswordIsSquare = true;

    /**
     * 间距，如果是正方形则不生效，取计算值
     */
    private int mInterval = 20;
    private Paint mPaintCircle;
    private Paint mPaintBorder;

    private int mWidth,mHeight;
    /**
     * 单个密码矩形框 宽高
     */
    private int mRectWH;
    private int mRectW, mRectH;

    /**
     * 输入密码个数
     */
    private int mInputSize;

    /**
     * 输入内容
     */
    private String mInputText;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        AutoUtils.auto(this);
        init(context);
        initArry(context, attrs);
    }

    private void initArry(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PasswordView);
            mPasswordLength = typedArray.getInt(R.styleable.PasswordView_PasswordLength, mPasswordLength);
            mPasswordRadius = (int) typedArray.getDimension(R.styleable.PasswordView_PasswordRadius, mPasswordRadius);
            mPasswordColor = typedArray.getColor(R.styleable.PasswordView_PasswordColor, mPasswordColor);
            mPasswordBorderColor = typedArray.getColor(R.styleable.PasswordView_PasswordBorderColor, mPasswordBorderColor);
            mPasswordBorderAll = typedArray.getBoolean(R.styleable.PasswordView_PasswordBorderAll, mPasswordBorderAll);
            mInterval = typedArray.getDimensionPixelSize(R.styleable.PasswordView_PasswordInterval, mInterval);
            mPasswordIsSquare = typedArray.getBoolean(R.styleable.PasswordView_PasswordIsSquare, mPasswordIsSquare);
            typedArray.recycle();
        }
    }

    private void init(Context context) {
        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setColor(mPasswordColor);

        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setColor(mPasswordBorderColor);
        mPaintBorder.setStrokeWidth(2);

        setBackgroundColor(0x00000000);
        setCursorVisible(false);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUI(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    public String getInputText() {
        return mInputText;
    }

    private void updateUI(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mInputSize = 0;
        }else {
            mInputSize = text.length();
        }
        if (mInputSize <= mPasswordLength) {
            mInputText = text.toString();
            invalidate();
        }

    }



    @Override
    protected void onDraw(Canvas canvas) {
        drawBorder(canvas);
    }

    private void drawBorder(Canvas canvas) {
        for (int i = 0; i < mPasswordLength; i++) {
            if (mPasswordBorderAll) {
                if (mPasswordIsSquare) {
                    canvas.drawRect(getPaddingLeft() + (mRectWH+mInterval) * i, getPaddingTop(),getPaddingLeft() + (mRectWH+mInterval) * i + mRectWH, mHeight - getPaddingBottom(), mPaintBorder);
                }else {
                    canvas.drawRect(getPaddingLeft() + (mRectW+mInterval) * i, getPaddingTop(),getPaddingLeft() + (mRectW+mInterval) * i + mRectW, mHeight - getPaddingBottom(), mPaintBorder);
                }
            }else {
                if (mPasswordIsSquare) {
                    canvas.drawLine(getPaddingLeft() + (mRectWH+mInterval) * i, mHeight - getPaddingBottom(), getPaddingLeft() + (mRectWH+mInterval) * i + mRectWH, mHeight - getPaddingBottom(), mPaintBorder);
                }else {
                    canvas.drawLine(getPaddingLeft() + (mRectW+mInterval) * i, mHeight - getPaddingBottom(), getPaddingLeft() + (mRectW+mInterval) * i + mRectW, mHeight - getPaddingBottom(), mPaintBorder);
                }
            }

        }

        for (int i = 0; i < mInputSize; i++) {
            if (mPasswordIsSquare) {
                canvas.drawCircle(getPaddingLeft() + (mRectWH+mInterval) * i + mRectWH /2, getPaddingTop()+ mRectWH/2, mPasswordRadius,mPaintCircle);
            } else {
                canvas.drawCircle(getPaddingLeft() + (mRectW+mInterval) * i + mRectW /2, getPaddingTop()+ mRectH/2, mPasswordRadius,mPaintCircle);

            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        if (mPasswordIsSquare) {
            mRectWH = mHeight - getPaddingBottom() - getPaddingTop();
            mInterval = (mWidth - getPaddingLeft() - getPaddingRight() - mRectWH * mPasswordLength) /(mPasswordLength-1);
        }else {
            mRectH = mHeight - getPaddingBottom() - getPaddingTop();
            mRectW = (mWidth - getPaddingLeft() - getPaddingRight() - mInterval * (mPasswordLength - 1)) / mPasswordLength;
        }
    }
}

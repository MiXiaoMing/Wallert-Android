package com.qiumi.app.support.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.qiumi.app.support.R;

/**
 * 自定义的字体显示组件
 */

public class FontTextView extends TextView {

    public FontTextView(Context context) {
        super(context);
        init("");
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        String fontName = "";
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        if (typedArray != null) {
            String font = typedArray.getString(R.styleable.FontTextView_fontName);
            if (!TextUtils.isEmpty(font)) {
                fontName = font;
            }
        }
        init(fontName);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);

        String fontName = "";
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        if (typedArray != null) {
            String font = typedArray.getString(R.styleable.FontTextView_fontName);
            if (!TextUtils.isEmpty(font)) {
                fontName = font;
            }
        }
        init(fontName);
    }

    private void init(String fontName) {
        Typeface tf = null;
        if (TextUtils.isEmpty(fontName)) {
            tf = TypefaceHelper.get(this.getContext(), getResources().getString(R.string.font_pf_regular));
        } else {
            tf = TypefaceHelper.get(this.getContext(), fontName);
        }
        setTypeface(tf);
    }
}

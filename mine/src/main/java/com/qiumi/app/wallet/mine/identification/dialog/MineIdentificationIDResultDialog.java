package com.qiumi.app.wallet.mine.identification.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.qiumi.app.support.component.BaseViewInterface;
import com.qiumi.app.wallet.mine.R;

/**
 * 身份认证dialog。
 *
 * @author jiangkun
 * @date 2019/9/12
 */

public class MineIdentificationIDResultDialog extends Dialog implements BaseViewInterface {
    /** 成功、失败等信息提示 */
    private TextView mTvHint;
    /** 确定按钮 */
    private TextView mTvConfirmButton;
    private OnConfirmClickListener mOnConfirmClickListener;

    public MineIdentificationIDResultDialog(@NonNull Context context) {
        super(context);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.mine_identification_id_result_dialog);
            initData();
            initView(context);
            initListener();
            setDataToView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(Context context) {
        try {
            mTvHint = findViewById(R.id.tv_hint);
            mTvConfirmButton = findViewById(R.id.tv_confirm_button);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        try {
            mTvConfirmButton.setOnClickListener(v -> {
                try {
                    if (mOnConfirmClickListener != null) {
                        mOnConfirmClickListener.onClick();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataToView() {

    }

    public interface OnConfirmClickListener {
        /** 确定 */
        void onClick();
    }

    public void setOnConfirmClickListener(OnConfirmClickListener mOnConfirmClickListener) {
        this.mOnConfirmClickListener = mOnConfirmClickListener;
    }
}

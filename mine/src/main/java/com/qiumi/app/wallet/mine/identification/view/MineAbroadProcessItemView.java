package com.qiumi.app.wallet.mine.identification.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiumi.app.support.component.BaseViewInterface;
import com.qiumi.app.wallet.mine.R;

/**
 * 海外-高级信息认证-进度条itemView。
 */

public class MineAbroadProcessItemView extends LinearLayout implements BaseViewInterface{
    /** 左边横线 */
    private View mLeftLine;
    /** 中间圆圈 */
    private View mMiddleCircle;
    /** 右边横线 */
    private View mRightLine;
    /** 名称 */
    private TextView mTvName;

    public MineAbroadProcessItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        try {
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
            LayoutInflater.from(context).inflate(R.layout.mine_abroad_process_item_view, this);
            mLeftLine = findViewById(R.id.left_line);
            mMiddleCircle = findViewById(R.id.middle_circle);
            mRightLine = findViewById(R.id.right_line);
            mTvName = findViewById(R.id.tv_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void setDataToView() {

    }

    public void hideLeftLine() {
        try {
            mLeftLine.setVisibility(INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideRightLine() {
        try {
            mRightLine.setVisibility(INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showName(String name) {
        try {
            mTvName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void active() {
        mLeftLine.setBackgroundColor(getResources().getColor(R.color.mine_identify_abroad_high_level_process_horizontal_line_active));
        mMiddleCircle.setBackgroundResource(R.drawable.mine_abroad_process_circle_active);
        mRightLine.setBackgroundColor(getResources().getColor(R.color.mine_identify_abroad_high_level_process_horizontal_line_active));
        mTvName.setTextColor(getResources().getColor(R.color.mine_identify_abroad_high_level_process_horizontal_line_active));
    }

    public void unActive() {
        mLeftLine.setBackgroundColor(getResources().getColor(R.color.mine_identify_abroad_high_level_process_horizontal_line_un_active));
        mMiddleCircle.setBackgroundResource(R.drawable.mine_abroad_process_circle_un_active);
        mRightLine.setBackgroundColor(getResources().getColor(R.color.mine_identify_abroad_high_level_process_horizontal_line_un_active));
        mTvName.setTextColor(getResources().getColor(R.color.mine_identify_abroad_high_level_process_horizontal_line_un_active));
    }
}

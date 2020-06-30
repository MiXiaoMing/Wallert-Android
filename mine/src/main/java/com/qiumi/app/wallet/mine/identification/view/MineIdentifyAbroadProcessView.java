package com.qiumi.app.wallet.mine.identification.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.qiumi.app.support.component.BaseViewInterface;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.identification.activity.abroad.MineIdentifyAbroadHighLevelVerifyActivity;

/**
 * 国外高级信息认证-进度条。（基本信息、证件信息、住址信息、住址证明）
 *
 * @author jiangkun
 * @date 2019/9/16
 */

public class MineIdentifyAbroadProcessView extends LinearLayout implements BaseViewInterface {
    /**
     * 基本信息
     */
    private MineAbroadProcessItemView mBaseInfoView;
    /**
     * 证件信息
     */
    private MineAbroadProcessItemView mCardInfoView;
    /**
     * 住址信息
     */
    private MineAbroadProcessItemView mAddressInfoView;
    /**
     * 住址证明
     */
    private MineAbroadProcessItemView mAddressProveView;

    public MineIdentifyAbroadProcessView(Context context, @Nullable AttributeSet attrs) {
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
            LayoutInflater.from(context).inflate(R.layout.mine_identify_abroad_high_level_process_view, this);
            mBaseInfoView = findViewById(R.id.base_info_view);
            mCardInfoView = findViewById(R.id.card_info_view);
            mAddressInfoView = findViewById(R.id.address_info_view);
            mAddressProveView = findViewById(R.id.address_prove_view);
            // 设置名称。
            mBaseInfoView.showName(getResources().getString(R.string.mine_abroad_process_base_info));
            mCardInfoView.showName(getResources().getString(R.string.mine_abroad_process_card_info));
            mAddressInfoView.showName(getResources().getString(R.string.mine_abroad_process_address_info));
            mAddressProveView.showName(getResources().getString(R.string.mine_abroad_process_address_prove));
            // 基本信息不显示左边横线。
            mBaseInfoView.hideLeftLine();
            // 住址证明不显示右边横线。
            mAddressProveView.hideRightLine();
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

    /**
     * 更新进度。
     *
     * @param tag 进度key
     */
    public void updateProcess(String tag) {
        try {
            switch (tag) {
                case MineIdentifyAbroadHighLevelVerifyActivity.TAG_BASE_INFO:
                    // 基本信息。
                    mBaseInfoView.active();
                    mCardInfoView.unActive();
                    mAddressInfoView.unActive();
                    mAddressProveView.unActive();
                    break;
                case MineIdentifyAbroadHighLevelVerifyActivity.TAG_CARD_INFO:
                    // 证件信息。
                    mBaseInfoView.active();
                    mCardInfoView.active();
                    mAddressInfoView.unActive();
                    mAddressProveView.unActive();
                    break;
                case MineIdentifyAbroadHighLevelVerifyActivity.TAG_ADDRESS_INFO:
                    // 住址信息。
                    mBaseInfoView.active();
                    mCardInfoView.active();
                    mAddressInfoView.active();
                    mAddressProveView.unActive();
                    break;
                case MineIdentifyAbroadHighLevelVerifyActivity.TAG_ADDRESS_PROVE:
                    // 住址证明。
                    mBaseInfoView.active();
                    mCardInfoView.active();
                    mAddressInfoView.active();
                    mAddressProveView.active();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.qiumi.app.wallet.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.AutoBaseFragment;
import com.qiumi.app.support.component.HeaderView;
import com.qiumi.app.wallet.wallet.adapter.MessageAdapter;

import java.util.ArrayList;

/**
 * Created by ThinkPad on 2019/9/26.
 */

public class MessageActivity extends AutoBaseActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    private ArrayList<String> mTitles;
    private HeaderView hvHeader;
    private ViewPager vpMessage;
    private ArrayList<AutoBaseFragment> mFragment;

    private TextView tvAssetMessage;
    private TextView tvOtherMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        initData();
    }

    private void initData() {

        tvAssetMessage.setSelected(true);
        tvOtherMessage.setSelected(false);
        tvAssetMessage.setOnClickListener(this);
        tvOtherMessage.setOnClickListener(this);


        mFragment = new ArrayList<>();
        mFragment.add(new AssetFragment());
        mFragment.add(new AssetFragment());

        MessageAdapter mAdapter = new MessageAdapter(getSupportFragmentManager(), mFragment);
        vpMessage.setAdapter(mAdapter);
        vpMessage.addOnPageChangeListener(this);

    }

    private void initView() {
        hvHeader = findViewById(R.id.hvHeader);
        tvAssetMessage = findViewById(R.id.tvAssetMessage);
        tvOtherMessage = findViewById(R.id.tvOtherMessage);
        vpMessage = findViewById(R.id.vpMessage);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tvAssetMessage.setSelected(true);
            tvOtherMessage.setSelected(false);
        }else {
            tvAssetMessage.setSelected(false);
            tvOtherMessage.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvAssetMessage) {
            vpMessage.setCurrentItem(0);
        } else if (v.getId() == R.id.tvOtherMessage) {
            vpMessage.setCurrentItem(1);
        }
    }
}

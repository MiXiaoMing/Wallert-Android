package com.qiumi.app.wallet.wallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiumi.app.support.AutoBaseFragment;

/**
 * Created by ThinkPad on 2019/9/27.
 */

public class AssetFragment extends AutoBaseFragment {
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_asset, container, false);
        return mRootView;
    }
}

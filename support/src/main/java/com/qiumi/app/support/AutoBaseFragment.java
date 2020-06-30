package com.qiumi.app.support;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appframe.utils.logger.Logger;

/**
 * Fragment基类。
 * <p>
 * 本身不需要自适配，AutoBaseActivity已经自适配了
 *
 * @author jiangkun
 * @date 2019/8/22
 */

public class AutoBaseFragment extends Fragment {
    protected Activity mActivity;
    protected View mRootView;

    /**
     * 获得全局的，防止使用getActivity()为空
     *
     * @param context context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 方便控制台查看。
        Logger.getLogger().d(getClass().getSimpleName() + " onCreate.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 方便控制台查看。
        Logger.getLogger().d(getClass().getSimpleName() + " onDestroy.");
    }

    public void result(String path) {

    }
}

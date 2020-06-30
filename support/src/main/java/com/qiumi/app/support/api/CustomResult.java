package com.qiumi.app.support.api;

import android.text.TextUtils;

import com.appframe.framework.http.HttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.app.AppRuntimeUtil;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.R;
import com.qiumi.app.support.utils.ActivityUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class CustomResult<T extends HttpResult> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T result) {
        int status = Integer.valueOf(result.getStatus());
        if (status <= 0) {
            Logger.getLogger().e("网络请求错误：，status：" + result.getStatus() + "  message：" + result.getMessage());
            if (ActivityUtil.isAvailable(AppRuntimeUtil.getInstance().getCurrentActivity())) {
                if (!TextUtils.isEmpty(result.getMessage()))
                    AFToast.showShort(AppRuntimeUtil.getInstance().getCurrentActivity(), result.getMessage());
                else
                    AFToast.showShort(AppRuntimeUtil.getInstance().getCurrentActivity(), R.string.netword_fail);
            }

            onError();
        } else {
            onSuccess(result);
        }
    }

    @Override
    public void onError(Throwable e) {
        Logger.getLogger().e("网络请求失败：" + e.getMessage());
        if (e.getMessage().contains("HTTP 401")) {
            if (ActivityUtil.isAvailable(AppRuntimeUtil.getInstance().getCurrentActivity())) {
                AFToast.showShort(AppRuntimeUtil.getInstance().getCurrentActivity(), R.string.token_expired);
            }
        } else {
            if (ActivityUtil.isAvailable(AppRuntimeUtil.getInstance().getCurrentActivity())) {
                AFToast.showShort(AppRuntimeUtil.getInstance().getCurrentActivity(), R.string.netword_fail);
            }
        }
        onError();
    }

    @Override
    public void onComplete() {

    }

    /**
     * 错误回调
     */
    public abstract void onError();

    /**
     * 正确回调
     */
    public abstract void onSuccess(T result);
}

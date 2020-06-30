package com.qiumi.app.support.common.upload;

/**
 * 回调基类
 */
public interface UploadCallback<T> {

    void onStart(String uuid);

    void onProcess(int process);

    void onSuccess(T entity);

    void onFailed(String errMsg);
}

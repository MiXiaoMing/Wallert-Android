package com.qiumi.app.support.common.upload;

import android.annotation.SuppressLint;

import com.appframe.library.network.http.AFHttpClient;
import com.appframe.utils.logger.Logger;
import com.google.gson.Gson;
import com.qiumi.app.support.utils.UserUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 实现文件上传管理器
 * 实现功能：单文件上传（toWeb，toC)，取消文件上传
 */

public class UploadManager {

    public static final int UPLOAD_FORM = 1;
    public static final int UPLOADFILE_FORM = 2;
    public static final int UPLOADFILE_TOC = 3;
    public static final int UPLOAD_LARGE = 4;

    private long preTime = 0L;
    private long curTime = 0L;

    private static UploadManager instance = null;

    public static UploadManager getInstance() {
        if (instance == null) {
            synchronized (UploadManager.class) {
                instance = new UploadManager();
            }
        }
        return instance;
    }

    private Map<String, Integer> failTimesMap = new HashMap<>();
    private Call currentCall;


    /**
     * 上传文件，不同点是在body中添加fileName字段
     *
     * @param url
     * @param callback
     */
    public void uploadFile(final String url, final String frontFilePath, final String backFilePath, final UploadCallback<UploadResultEntity> callback) {
        callback.onStart("开始上传文件");

//        final String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

        //            final byte[] bytes = new byte[512 * 1024];
//            final InputStream is = new FileInputStream(filePath);
//
//            File file = new File(filePath);
//            final long totalSize = file.length();
//            final long[] currentSize = {0};

//            RequestBody requestBody = new RequestBody() {
//                @Override
//                public MediaType contentType() {
//                    return MediaType.parse("application/octet-stream");
//                }
//
//                @Override
//                public void writeTo(BufferedSink sink) throws IOException {
//                    int len = 0;
//                    while ((len = is.read(bytes)) != -1) {
//                        sink.write(bytes, 0, len);
//                        currentSize[0] += len;
//                        callback.onProcess((int) (currentSize[0] * 100 / totalSize));
//                    }
//                }
//            };

        MediaType formData = MediaType.parse("multipart/form-data");

        RequestBody bodyParams = RequestBody.create(formData, "");

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", "", bodyParams);

        //循环添加文件
        {
            File file1 = new File(frontFilePath);
            requestBodyBuilder.addFormDataPart("front", file1.getName(), RequestBody.create(formData, new File(frontFilePath)));
        }
        {
            File file1 = new File(backFilePath);
            requestBodyBuilder.addFormDataPart("reverse", file1.getName(), RequestBody.create(formData, new File(backFilePath)));
        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyBuilder.build())
                .addHeader("token", UserUtil.getToken())
                .build();

        if (currentCall != null) {
            currentCall = null;
        }
        currentCall = AFHttpClient.getInstance().newCall(request);

        currentCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onFailed("上传失败：" + e.getMessage(), callback);
                currentCall = null;
            }

            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Logger.getLogger().e("—Unexpected code " + response);
                    onFailed("未知错误[" + response + "]", callback);
                    currentCall = null;
                    return;
                }

                String uploadResult = new String(response.body().bytes(), "utf-8");
                Logger.getLogger().d("—上传接口返回成功：" + uploadResult);

                Gson gson = new Gson();
                UploadResultEntity resultEntity = gson.fromJson(uploadResult, UploadResultEntity.class);
                if (resultEntity != null && resultEntity.status != null) {
                    if (!resultEntity.status.equals("1")) {
                        onFailed("上传结果返回错误", callback);
                    } else {
                        Observable.just(resultEntity).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<UploadResultEntity>() {
                            @Override
                            public void accept(UploadResultEntity entity) throws Exception {
                                callback.onSuccess(entity);
                            }
                        });
                    }
                } else {
                    onFailed("上传失败：有空指针出现", callback);
                }
                currentCall = null;
            }
        });

    }

    //返回错误信息
    @SuppressLint("CheckResult")
    private void onFailed(String errMsg, final UploadCallback callback) {
        Observable.just(errMsg).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                callback.onFailed(errMsg);
            }
        });
    }

}

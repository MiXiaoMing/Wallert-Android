package com.qiumi.app.wallet.mine.identification.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.aoglrithm.Random;
import com.appframe.utils.logger.Logger;
import com.bumptech.glide.Glide;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.api.ServerHosts;
import com.qiumi.app.support.common.upload.UploadCallback;
import com.qiumi.app.support.common.upload.UploadManager;
import com.qiumi.app.support.common.upload.UploadResultEntity;
import com.qiumi.app.support.component.action_sheet.ActionSheetDialog;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.support.utils.FileUtil;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.api.input.CreateBioSessionBody;
import com.qiumi.app.wallet.mine.api.input.SubmitMainlandBody;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;
import com.qiumi.app.wallet.mine.api.output.CreateBioSessionEntity;
import com.qiumi.app.wallet.mine.identification.activity.abroad.MineIdentifyAbroadIDCardVerifyActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 基本实名认证页-上传身份证。
 */

public class MineIdentifyIDCardVerifyActivity extends AutoBaseActivity {

    private ImageView ivFront, ivBack;
    private String mPhotoFilePath, mDstPhotoPath;

    private int type = 0; //1:正面 2：背面
    private String frontPath, backPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_identification_upload_id_card);
        initView();
    }

    private void initView() {
        findViewById(R.id.rlyBack).setOnClickListener(clickListener);

        findViewById(R.id.flyFront).setOnClickListener(clickListener);
        findViewById(R.id.flyBack).setOnClickListener(clickListener);

        ivFront = findViewById(R.id.ivFront);
        ivBack = findViewById(R.id.ivBack);

        findViewById(R.id.tv_upload_button).setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.rlyBack) {
                finish();
            } else if (id == R.id.flyFront) {
                type = 1;
                showPictureChooseDialog();
            } else if (id == R.id.flyBack) {
                type = 2;
                showPictureChooseDialog();
            } else if (id == R.id.tv_upload_button) {
                uploadFile();

                // TODO: 2019/10/17 其实需要进行face++认证 ,获取图片信息
                // 打开基本实名认证页-上传身份证-识别后。
//                Intent intent = new Intent(MineIdentifyIDCardVerifyActivity.this, MineIdentifyIDCardVerifyResultActivity.class);
//                startActivity(intent);
//
//                finish();
            }
        }
    };

    /**
     * 显示图片选择弹框
     */
    // TODO: 2019/10/17 需要添加读取和写入权限
    private void showPictureChooseDialog() {
        ActionSheetDialog dialog = new ActionSheetDialog(this);
        dialog.builder()
                .addSheetItem("拍摄", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        dialog.dismiss();

                        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
                        mPhotoFilePath = FileUtil.getMediaCachePath(MineIdentifyIDCardVerifyActivity.this) + File.separator + fileName;
                        File out = new File(mPhotoFilePath);
                        mDstPhotoPath = mPhotoFilePath;
                        Uri uri = FileProvider.getUriForFile(MineIdentifyIDCardVerifyActivity.this, "com.qiumi.app.wallet.fileprovider", out);
                        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(imageCaptureIntent, 1);
                    }
                })
                .addSheetItem("从相册选取", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        dialog.dismiss();

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        mDstPhotoPath = FileUtil.getMediaCachePath(MineIdentifyIDCardVerifyActivity.this) + File.separator + fileName + ".jpg";
                        startActivityForResult(intent, 2);
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (!FileUtil.isExistsFile(mDstPhotoPath) || FileUtil.getFileSize(mDstPhotoPath) == 0) {
                return;
            }
            Logger.getLogger().d("拍照返回图片路径:" + mDstPhotoPath);
            if (type == 1) {
                frontPath = mDstPhotoPath;
                Glide.with(MineIdentifyIDCardVerifyActivity.this).load(mDstPhotoPath).into(ivFront);
            } else {
                backPath = mDstPhotoPath;
                Glide.with(MineIdentifyIDCardVerifyActivity.this).load(mDstPhotoPath).into(ivBack);
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getContentResolver();
            final String[] selectColumn = {MediaStore.Images.Media.DATA};
            String filePath = uri.getPath();
            Cursor cursor = cr.query(uri, selectColumn, null, null, null);
            if (FileUtil.isExistsFile(filePath) || FileUtil.getFileSize(filePath) > 0) {
                if (filePath.endsWith("jpg") || filePath.endsWith("jpeg") || filePath.endsWith("png")
                        || filePath.endsWith("gif") || filePath.endsWith("bmp")) {
                    mPhotoFilePath = filePath;

                    if (type == 1) {
                        frontPath = mPhotoFilePath;
                        Glide.with(MineIdentifyIDCardVerifyActivity.this).load(Uri.fromFile(new File(mPhotoFilePath))).into(ivFront);
                    } else {
                        backPath = mPhotoFilePath;
                        Glide.with(MineIdentifyIDCardVerifyActivity.this).load(Uri.fromFile(new File(mPhotoFilePath))).into(ivBack);
                    }
                }
            } else if (null != cursor) {
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    String tempFilePath = cursor.getString(0).toLowerCase();
                    if (null != cursor && !cursor.isClosed()) {
                        cursor.close();
                    }
                    if (!FileUtil.isExistsFile(tempFilePath) || FileUtil.getFileSize(tempFilePath) == 0) {
                        return;
                    }
                    if (tempFilePath.endsWith("jpg") || tempFilePath.endsWith("jpeg") || tempFilePath.endsWith("png")
                            || tempFilePath.endsWith("gif") || tempFilePath.endsWith("bmp")) {
                        mPhotoFilePath = tempFilePath;

                        Logger.getLogger().d("图片地址：" + mPhotoFilePath);
                        if (type == 1) {
                            frontPath = mPhotoFilePath;
                            Glide.with(MineIdentifyIDCardVerifyActivity.this).load(Uri.fromFile(new File(mPhotoFilePath))).into(ivFront);
                        } else {
                            backPath = mPhotoFilePath;
                            Glide.with(MineIdentifyIDCardVerifyActivity.this).load(Uri.fromFile(new File(mPhotoFilePath))).into(ivBack);
                        }
                    }
                }
            } else {
                AFToast.showShort(this, "照片未找到");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile() {
        if (TextUtils.isEmpty(frontPath) || TextUtils.isEmpty(backPath)) {
            AFToast.showShort(this, "照片不能为空");
            return;
        }
        UploadManager.getInstance().uploadFile(ServerHosts.service_wallet + "/security/kyc/1/upload/mainland", frontPath, backPath,
                new UploadCallback<UploadResultEntity>() {
                    @Override
                    public void onStart(String uuid) {

                    }

                    @Override
                    public void onProcess(int process) {

                    }

                    @Override
                    public void onSuccess(UploadResultEntity entity) {
                        String fileToken = entity.data.fileCode;
                        if (!TextUtils.isEmpty(fileToken)) {
                            Intent intent = new Intent(MineIdentifyIDCardVerifyActivity.this, MineIdentifyIDCardVerifyMoreActivity.class);
                            intent.putExtra("fileToken", fileToken);
                            startActivity(intent);

                            finish();
                        } else {
                            AFToast.showShort(MineIdentifyIDCardVerifyActivity.this, "文件上传失败，点击重试");
                        }
                    }

                    @Override
                    public void onFailed(String errMsg) {

                    }
                });

    }
}

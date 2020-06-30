package com.qiumi.app.wallet.mine.identification.activity.abroad;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.RelativeLayout;

import com.appframe.framework.http.EmptyHttpResult;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.AutoBaseFragment;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.component.action_sheet.ActionSheetDialog;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.support.utils.FileUtil;
import com.qiumi.app.wallet.mine.R;
import com.qiumi.app.wallet.mine.api.iterfaces.MineDataManager;
import com.qiumi.app.wallet.mine.identification.fragment.MineAbroadAddressInfoFragment;
import com.qiumi.app.wallet.mine.identification.fragment.MineAbroadAddressProveFragment;
import com.qiumi.app.wallet.mine.identification.fragment.MineAbroadBaseInfoFragment;
import com.qiumi.app.wallet.mine.identification.fragment.MineAbroadCardInfoFragment;
import com.qiumi.app.wallet.mine.identification.view.MineIdentifyAbroadProcessView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 海外-高级信息认证页。
 */

public class MineIdentifyAbroadHighLevelVerifyActivity extends AutoBaseActivity {
    /**
     * 进度条：4步
     */
    private MineIdentifyAbroadProcessView mProcessView;
    /**
     * 管理Fragment
     */
    private FragmentManager mFragmentManager;
    /**
     * 基本信息Fragment
     */
    private MineAbroadBaseInfoFragment mBaseInfoFragment;
    /**
     * 证件信息Fragment
     */
    private MineAbroadCardInfoFragment mCardInfoFragment;
    /**
     * 住址信息Fragment
     */
    private MineAbroadAddressInfoFragment mAddressInfoFragment;
    /**
     * 住址证明Fragment
     */
    private MineAbroadAddressProveFragment mAddressProveFragment;
    /**
     * 进度key
     */
    public static final String TAG_BASE_INFO = "base_info";
    public static final String TAG_CARD_INFO = "card_info";
    public static final String TAG_ADDRESS_INFO = "address_info";
    public static final String TAG_ADDRESS_PROVE = "address_prove";

    private String country, firstName, middleName, lastName;
    private String idCardFront, idCardBack;
    private String address, addressDetail, city, code, addressProve;

    private String mPhotoFilePath, mDstPhotoPath;
    private AutoBaseFragment currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity_identify_abroad_high_level_verify);
        initView();
    }

    public void initView() {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);
        rlyBack.setOnClickListener(clickListener);

        mProcessView = findViewById(R.id.process_view);
        mFragmentManager = getSupportFragmentManager();
        mBaseInfoFragment = new MineAbroadBaseInfoFragment();
        mCardInfoFragment = new MineAbroadCardInfoFragment();
        mAddressInfoFragment = new MineAbroadAddressInfoFragment();
        mAddressProveFragment = new MineAbroadAddressProveFragment();

        updateProcess(TAG_BASE_INFO);
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.rlyBack) {
                finish();
            }
        }
    };

    /**
     * 更新进度。
     *
     * @param tag 进度标识
     */
    public void updateProcess(String tag) {
        switch (tag) {
            case TAG_BASE_INFO:
                // 基本信息
                mFragmentManager.beginTransaction().replace(R.id.fl_container, mBaseInfoFragment).commit();
                mProcessView.updateProcess(TAG_BASE_INFO);
                break;
            case TAG_CARD_INFO:
                // 证件信息
                mFragmentManager.beginTransaction().replace(R.id.fl_container, mCardInfoFragment).commit();
                mProcessView.updateProcess(TAG_CARD_INFO);
                break;
            case TAG_ADDRESS_INFO:
                // 住址信息
                mFragmentManager.beginTransaction().replace(R.id.fl_container, mAddressInfoFragment).commit();
                mProcessView.updateProcess(TAG_ADDRESS_INFO);
                break;
            case TAG_ADDRESS_PROVE:
                // 住址证明
                mFragmentManager.beginTransaction().replace(R.id.fl_container, mAddressProveFragment).commit();
                mProcessView.updateProcess(TAG_ADDRESS_PROVE);
                break;
            default:
                break;
        }
    }

    public void updateBaseInfo(String country, String firstName, String middleName, String lastName) {
        this.country = country;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public void updateCardInfo(String idCardFront, String idCardBack) {
        this.idCardFront = idCardFront;
        this.idCardBack = idCardBack;
    }

    public void updateAddress(String address, String addressDetail, String city, String code) {
        this.address = address;
        this.addressDetail = addressDetail;
        this.city = city;
        this.code = code;
    }

    public void updateAddressProve(String addressProve) {
        this.addressProve = addressProve;
    }

    /**
     * 显示图片选择弹框
     */
    public void showPictureChooseDialog(AutoBaseFragment fragment) {
        this.currentFragment = fragment;

        ActionSheetDialog dialog = new ActionSheetDialog(this);
        dialog.builder()
                .addSheetItem("拍摄", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        dialog.dismiss();

                        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
                        mPhotoFilePath = FileUtil.getMediaCachePath(MineIdentifyAbroadHighLevelVerifyActivity.this) + File.separator + fileName;
                        File out = new File(mPhotoFilePath);
                        mDstPhotoPath = mPhotoFilePath;
                        Uri uri = FileProvider.getUriForFile(MineIdentifyAbroadHighLevelVerifyActivity.this, "com.qiumi.app.wallet.fileprovider", out);
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
                        mDstPhotoPath = FileUtil.getMediaCachePath(MineIdentifyAbroadHighLevelVerifyActivity.this) + File.separator + fileName + ".jpg";
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
            if (currentFragment != null) {
                currentFragment.result(mDstPhotoPath);
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

                    if (currentFragment != null) {
                        currentFragment.result(mPhotoFilePath);
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

                        if (currentFragment != null) {
                            currentFragment.result(mPhotoFilePath);
                        }
                    }
                }
            } else {
                AFToast.showShort(this, "照片未找到");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 提交信息
     */
    public void submit() {
        new MineDataManager()
                .abroadHighLevelVerify()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<EmptyHttpResult>() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(EmptyHttpResult result) {
                        if (ActivityUtil.isAvailable(MineIdentifyAbroadHighLevelVerifyActivity.this)) {
                            AFToast.showShort(MineIdentifyAbroadHighLevelVerifyActivity.this, R.string.verify_success);
                            finish();
                        }
                    }
                });
    }
}

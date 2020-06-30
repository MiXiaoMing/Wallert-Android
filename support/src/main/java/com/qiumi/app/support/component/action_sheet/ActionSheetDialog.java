package com.qiumi.app.support.component.action_sheet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qiumi.app.support.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dwb on 2018/1/4.
 * describe1:
 * describe2: 仿ios底部同框
 * email:wolfking0608@163.com
 *
 * @author dwb
 * @date 2019/9/11
 */

public class ActionSheetDialog {
    private Context mContext;
    private Dialog mDialog;
    private TextView mTvTitle;
    private LinearLayout mLlLayoutContent;
    private ScrollView mSlLayoutContent;
    private boolean mIsShowTitle = false;
    private List<SheetItem> mSheetItemList;
    private Display mDisplay;

    public ActionSheetDialog(Context context) {
        try {
            this.mContext = context;
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                mDisplay = windowManager.getDefaultDisplay();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 初始化View */
    public ActionSheetDialog builder() {
        try {
            // 获取Dialog布局
            View view = LayoutInflater.from(mContext).inflate(R.layout.actionsheet_dialog_view, null);

            // 设置Dialog最小宽度为屏幕宽度
            view.setMinimumWidth(mDisplay.getWidth());

            // 获取自定义Dialog布局中的控件
            mSlLayoutContent = view.findViewById(R.id.sLayout_content);
            mLlLayoutContent = view.findViewById(R.id.lLayout_content);
            mTvTitle = view.findViewById(R.id.txt_title);

            // 取消按钮。
            TextView mTvCancel = view.findViewById(R.id.txt_cancel);
            mTvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // 定义Dialog布局和参数
            mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
            mDialog.setContentView(view);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.x = 0;
                lp.y = 0;
                dialogWindow.setAttributes(lp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /** 不要设置title，显示UI有问题 */
    private ActionSheetDialog setTitle(String title) {
        // todo 待解决
        try {
            mIsShowTitle = true;
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public ActionSheetDialog setCancelable(boolean cancel) {
        try {
            mDialog.setCancelable(cancel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        try {
            mDialog.setCanceledOnTouchOutside(cancel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * @param strItem  条目名称
     * @param color    条目字体颜色，设置null则默认蓝色
     * @param listener listener
     * @return ActionSheetDialog
     */
    public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color, OnSheetItemClickListener listener) {
        try {
            if (mSheetItemList == null) {
                mSheetItemList = new ArrayList<>();
            }
            mSheetItemList.add(new SheetItem(strItem, color, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setSheetItems() {
        try {
            if (mSheetItemList == null || mSheetItemList.size() <= 0) {
                return;
            }

            int size = mSheetItemList.size();

            // TODO 高度控制，非最佳解决办法
            // 添加条目过多的时候控制高度
            if (size >= 7) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSlLayoutContent.getLayoutParams();
                params.height = mDisplay.getHeight() / 2;
                mSlLayoutContent.setLayoutParams(params);
            }

            // 循环添加条目
            for (int i = 1; i <= size; i++) {
                final int index = i;
                SheetItem sheetItem = mSheetItemList.get(i - 1);
                String strItem = sheetItem.mName;
                SheetItemColor color = sheetItem.mColor;
                final OnSheetItemClickListener listener = sheetItem.mItemClickListener;

                TextView textView = new TextView(mContext);
                textView.setText(strItem);
                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER);

                // 背景图片
                if (size == 1) {
                    if (mIsShowTitle) {
                        textView.setBackgroundResource(R.drawable.bg_txt_stroke_white_bottom);
                    } else {
                        textView.setBackgroundResource(R.drawable.bg_txt_stroke_white_top);
                    }
                } else {
                    if (mIsShowTitle) {
                        if (i >= 1 && i < size) {
                            textView.setBackgroundResource(R.drawable.bg_txt_stroke_white_middle);
                        } else {
                            textView.setBackgroundResource(R.drawable.bg_txt_stroke_white_bottom);
                        }
                    } else {
                        if (i == 1) {
                            textView.setBackgroundResource(R.drawable.bg_txt_stroke_white_top);
                        } else if (i < size) {
                            textView.setBackgroundResource(R.drawable.bg_txt_stroke_white_middle);
                        } else {
                            textView.setBackgroundResource(R.drawable.bg_txt_stroke_white_bottom);
                        }
                    }
                }

                // 字体颜色
                if (color == null) {
                    textView.setTextColor(Color.parseColor(SheetItemColor.Blue.getName()));
                } else {
                    textView.setTextColor(Color.parseColor(color.getName()));
                }

                // 高度
                float scale = mContext.getResources().getDisplayMetrics().density;
                int height = (int) (45 * scale + 0.5f);
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

                // 点击事件
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            listener.onClick(index);
                            mDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                mLlLayoutContent.addView(textView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        try {
            setSheetItems();
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            mDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnSheetItemClickListener {
        /**
         * item点击
         * @param which which
         */
        void onClick(int which);
    }

    public class SheetItem {
        String mName;
        OnSheetItemClickListener mItemClickListener;
        SheetItemColor mColor;

        SheetItem(String name, SheetItemColor color, OnSheetItemClickListener itemClickListener) {
            this.mName = name;
            this.mColor = color;
            this.mItemClickListener = itemClickListener;
        }
    }

    public enum SheetItemColor {
        /**
         * 蓝色
         */
        Blue("#037BFF"),
        /**
         * 红色
         */
        Red("#FD4A2E");

        private String name;

        SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
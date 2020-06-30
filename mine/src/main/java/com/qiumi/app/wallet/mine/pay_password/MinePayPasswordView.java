package com.qiumi.app.wallet.mine.pay_password;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiumi.app.wallet.mine.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Android仿支付密码输入框。
 * <p>
 * GitHub地址：https://github.com/280357392/ZFPlay/blob/master/app/src/main/java/com/huatec/myapplication/MainActivity.java
 *
 * @author jiangkun
 * @date 2019/9/6
 */

public class MinePayPasswordView extends RelativeLayout {
    /**
     * 注意判空。
     */
    private Context context;
    /**
     * 输入的密码
     */
    private String strPassword;
    /**
     * 就6个输入框不会变了，用数组内存申请固定空间，比List省空间
     */
    private TextView[] tvList;
    /**
     * 用GridView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能
     */
    private GridView gridView;
    /**
     * 要用Adapter中适配，用数组不能往adapter中填充
     */
    private ArrayList<Map<String, String>> valueList;
    /**
     * 用于记录当前输入密码格位置
     */
    private int currentIndex = -1;
    /**
     * 空白键在键盘中的位置。
     */
    private static final int BLANK_KEY_POSITION = 9;
    /**
     * 返回键在键盘中的位置。
     */
    private static final int BACK_KEY_POSITION = 11;
    /**
     * 支付密码输入框个数。
     */
    private static final int INPUT_CELL_COUNT = 6;

    private TextView tvPasswordError;

    public MinePayPasswordView(Context context) {
        this(context, null);
    }

    public MinePayPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //view布局，注意3个参数的后2个，这样设置才行。
        View view = LayoutInflater.from(context).inflate(R.layout.mine_pay_password_view, this, false);
        valueList = new ArrayList<>();
        tvList = new TextView[6];
        //初始化控件
        tvList[0] = view.findViewById(R.id.tv_pass1);
        tvList[1] = view.findViewById(R.id.tv_pass2);
        tvList[2] = view.findViewById(R.id.tv_pass3);
        tvList[3] = view.findViewById(R.id.tv_pass4);
        tvList[4] = view.findViewById(R.id.tv_pass5);
        tvList[5] = view.findViewById(R.id.tv_pass6);

        tvPasswordError = view.findViewById(R.id.tvPasswordError);

        //初始化键盘
        gridView = view.findViewById(R.id.gv_keybord);
        //设置键盘显示按钮到集合
        setView();

        // 必须要，不然不显示控件
        addView(view);
    }

    /**
     * 设置按钮显示内容
     */
    private void setView() {
        // 键盘上按键的总数，10个数字（0-9），1个空白键，,1个删除键。
        final int keyCount = 12;
        // 初始化按钮上应该显示的数字
        for (int i = 1; i <= keyCount; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 10) {
                // 按键：1-9
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                // 按键：空白键
                map.put("name", "");
            } else if (i == 11) {
                // 按键：0
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                // 按键：删除键
                map.put("name", "X");
            }
            valueList.add(map);
        }

        //为键盘gridView设置适配器
        gridView.setAdapter(adapter);

        //为键盘按键添加点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // position范围：0-11
                // 点击0~9按钮（除去空白键和返回键）
                if (position < BACK_KEY_POSITION && position != BLANK_KEY_POSITION) {
                    // 判断输入位置————要小心数组越界
                    if (currentIndex >= -1 && currentIndex < INPUT_CELL_COUNT - 1) {
                        // 为下一个输入框设置值
                        tvList[++currentIndex].setText(valueList.get(position).get("name"));
                    }
                } else {
                    // 点击退格键
                    if (position == BACK_KEY_POSITION) {
                        // 判断是否删除完毕————要小心数组越界
                        if (currentIndex - 1 >= -1) {
                            tvList[currentIndex--].setText("");
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置监听方法，在第6位输入完成后触发
     *
     * @param onFinishInput onFinishInput
     */
    public void setOnFinishInput(final OnPasswordInputFinish onFinishInput) {
        tvList[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1) {
                    // 每次触发都要先将strPassword置空，再重新获取，避免由于输入删除再输入造成混乱
                    strPassword = "";
                    for (int i = 0; i < INPUT_CELL_COUNT; i++) {
                        strPassword += tvList[i].getText().toString().trim();
                    }
                    // 接口中要实现的方法，完成密码输入完成后的响应逻辑
                    onFinishInput.inputFinish();
                }
            }
        });
    }

    /**
     * 获取输入的密码
     *
     * @return 输入的密码
     */
    public String getStrPassword() {
        return strPassword;
    }

    /**
     * 清除密码
     */
    public void clearPassword() {
        if (tvList != null && tvList.length > 0) {
            for (int i = 0; i < tvList.length; ++i) {
                tvList[i].setText("");
            }
        }
        currentIndex = -1;
    }

    /**
     * 是否显示 提示信息
     */
    public void showPasswordError(boolean isShow) {
        if (isShow) {
            tvPasswordError.setVisibility(VISIBLE);
        } else {
            tvPasswordError.setVisibility(INVISIBLE);
        }
    }

    /**
     * GridView的适配器
     */
    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return valueList.size();
        }

        @Override
        public Object getItem(int position) {
            return valueList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                //装载数字键盘布局
                convertView = View.inflate(context, R.layout.mine_pay_password_key_bord_item_view, null);
                viewHolder = new ViewHolder();
                //初始化键盘按钮
                viewHolder.btnKey = convertView.findViewById(R.id.btn_keys);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //设置按钮显示数字
            viewHolder.btnKey.setText(valueList.get(position).get("name"));
            if (position == BLANK_KEY_POSITION) {
                //设置按钮背景
                viewHolder.btnKey.setBackgroundResource(R.drawable.selector_key_del);
                //设置按钮不可点击
                viewHolder.btnKey.setEnabled(false);
            }
            if (position == BACK_KEY_POSITION) {
                //设置按钮背景
                viewHolder.btnKey.setBackgroundResource(R.drawable.selector_key_del);
            }
            return convertView;
        }
    };

    public final class ViewHolder {
        TextView btnKey;
    }
}
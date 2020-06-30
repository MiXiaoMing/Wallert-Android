package com.qiumi.app.wallet.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.logger.Logger;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.qiumi.app.support.AutoBaseActivity;
import com.qiumi.app.support.Constants;
import com.qiumi.app.support.api.CustomResult;
import com.qiumi.app.support.component.FontTextView;
import com.qiumi.app.support.utils.ActivityUtil;
import com.qiumi.app.support.utils.SignUtil;
import com.qiumi.app.support.utils.UserUtil;
import com.qiumi.app.wallet.wallet.adapter.RecordAdapter;
import com.qiumi.app.wallet.wallet.api.input.AssetsRecordBody;
import com.qiumi.app.wallet.wallet.api.iterfaces.WalletDataManager;
import com.qiumi.app.wallet.wallet.api.output.Asset;
import com.qiumi.app.wallet.wallet.api.output.Record;
import com.qiumi.app.wallet.wallet.api.output.RecordsEntity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AssetRecordActivity extends AutoBaseActivity {

    private LinearLayout llyCoinType, llyOperType;
    private TextView tvAssetName, tvOperName;
    private ImageView ivAssetType, ivOperType;

    private LinearLayout llyOptionLayout, llyOptions;

    private LRecyclerView lrvRecords;
    private RecordAdapter recordAdapter;

    private List<Asset> assets = new ArrayList<>();
    private List<Constants.IDNamePair> operTypes = new ArrayList<>();

    private String curCoinType = "", curOperType = "", tmpType = "";
    private String curType = ""; // 空，不展示；01：展示货币类型；02：展示操作类型
    private ArrayList<TextView> options = new ArrayList<>();

    private String latestTranNo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_record);

        assets = (List<Asset>) getIntent().getSerializableExtra("assets");
        operTypes = (List<Constants.IDNamePair>) getIntent().getSerializableExtra("operTypes");

        initView();
    }

    private void initView() {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);

        llyCoinType = findViewById(R.id.llyCoinType);
        tvAssetName = findViewById(R.id.tvAssetName);
        ivAssetType = findViewById(R.id.ivAssetType);

        llyOperType = findViewById(R.id.llyOperType);
        tvOperName = findViewById(R.id.tvOperName);
        ivOperType = findViewById(R.id.ivOperType);

        llyOptionLayout = findViewById(R.id.llyOptionLayout);
        llyOptions = findViewById(R.id.llyOptions);
        TextView tvCancel = findViewById(R.id.tvCancel);
        TextView tvSure = findViewById(R.id.tvSure);

        initOption();

        lrvRecords = findViewById(R.id.lrvRecords);

        // 设置recycler view
        lrvRecords.setItemAnimator(new DefaultItemAnimator());
        lrvRecords.setLayoutManager(new LinearLayoutManager(this));

        recordAdapter = new RecordAdapter(this);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(recordAdapter);
        lrvRecords.setAdapter(adapter);
        adapter.setOnItemClickListener(itemClickListener);

        //设置头部加载颜色
        lrvRecords.setHeaderViewColor(R.color.gray_9f, R.color.gray_9f, android.R.color.transparent);
        //设置底部加载颜色
        lrvRecords.setFooterViewColor(R.color.gray_9f, R.color.gray_9f, android.R.color.transparent);
        //设置底部加载文字提示
        lrvRecords.setFooterViewHint("拼命加载中", "─── 我是有底线的 ───", "网络不给力啊，点击再试一次吧");

        // TODO: 2019/8/19 加载更多不起作用
        lrvRecords.setLoadMoreEnabled(true);
        lrvRecords.setOnRefreshListener(refreshListener);
        lrvRecords.setOnLoadMoreListener(loadMoreListener);
        lrvRecords.refresh();

        rlyBack.setOnClickListener(clickListener);
        llyCoinType.setOnClickListener(clickListener);
        llyOperType.setOnClickListener(clickListener);
        llyOptionLayout.setOnClickListener(clickListener);
        tvSure.setOnClickListener(clickListener);
        tvCancel.setOnClickListener(clickListener);
    }

    private void initOption() {
        // 特殊化处理
        // 三个展示效果，如果不存在，则不展示
        // 如果为1个，则展示，不能点击
        // 如果大于1个，则可点击，可展示选项
        if (assets == null || assets.size() <= 0) {
            llyCoinType.setVisibility(View.GONE);
            curCoinType = "";
        } else {
            curCoinType = assets.get(0).currencyId;
            tvAssetName.setText(assets.get(0).name);
        }

        if (operTypes == null || operTypes.size() <= 0) {
            llyOperType.setVisibility(View.GONE);
        } else {
            if (operTypes.size() == 1) {
                tvOperName.setText(operTypes.get(0).name);
                curOperType = operTypes.get(0).id;
            } else {
                tvOperName.setText(getResources().getString(R.string.type));
                curOperType = "";
            }
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.rlyBack) {
                finish();
            } else if (viewId == R.id.llyCoinType) {
                if (curType.equals("01")) {
                    llyOptionLayout.setVisibility(View.GONE);
                    curType = "";
                    changeAssetStatus(false);
                } else {
                    if (curType.equals("02")) {
                        llyOptionLayout.setVisibility(View.GONE);
                        curType = "";
                        changeOperStatus(false);
                    }
                    // 显示选项框，选项
                    llyOptionLayout.setVisibility(View.VISIBLE);
                    changeAssetStatus(true);
                    addAssetsOptions();
                }
            } else if (viewId == R.id.llyOperType) {
                if (curType.equals("02")) {
                    llyOptionLayout.setVisibility(View.GONE);
                    curType = "";
                    changeOperStatus(false);
                } else {
                    if (curType.equals("01")) {
                        llyOptionLayout.setVisibility(View.GONE);
                        curType = "";
                        changeAssetStatus(false);
                    }
                    changeOperStatus(true);
                    llyOptionLayout.setVisibility(View.VISIBLE);
                    addOperTypesOptions();
                }
            } else if (viewId == R.id.llyOptionLayout) {
                // 什么也不做，只是为了拦截点击事件
            } else if (viewId == R.id.tvCancel) {
                if (curType.equals("01")) {
                    changeAssetStatus(false);
                } else {
                    changeOperStatus(false);
                }
                llyOptionLayout.setVisibility(View.GONE);
                curType = "";
            } else if (viewId == R.id.tvSure) {
                if (curType.equals("01")) {
                    // 特殊化处理，必须要选择一种货币
                    if (TextUtils.isEmpty(tmpType)) {
                        AFToast.showShort(AssetRecordActivity.this, R.string.choice_coin_type);
                        return;
                    }
                    llyOptionLayout.setVisibility(View.GONE);
                    curType = "";
                    changeAssetStatus(false);
                    if (!tmpType.equals(curCoinType)) {
                        for (int i = 0; i < assets.size(); ++i) {
                            if (assets.get(i).currencyId.equals(tmpType)) {
                                tvAssetName.setText(assets.get(i).name);
                                curCoinType = tmpType;

                                // 重新刷新数据
                                lrvRecords.refresh();
                            }
                        }
                    }
                } else if (curType.equals("02")) {
                    llyOptionLayout.setVisibility(View.GONE);
                    curType = "";
                    changeOperStatus(false);
                    if (!tmpType.equals(curOperType)) {
                        curOperType = tmpType;
                        if (!TextUtils.isEmpty(tmpType)) {
                            tvOperName.setText(Constants.getOperName(curOperType));
                        } else {
                            tvOperName.setText(getResources().getString(R.string.type));
                        }
                        // 重新刷新数据
                        lrvRecords.refresh();
                    }
                }
            }
        }
    };

    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Record record = recordAdapter.getItem(position);
            if (null != record) {
                Intent intent = new Intent(AssetRecordActivity.this, RecordDetailActivity.class);
                intent.putExtra("transNo", record.transNo);
                intent.putExtra("operType", record.opType);
                startActivity(intent);
            }
        }
    };

    private OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            latestTranNo = null;
            recordAdapter.clear();

            getRecords();
        }
    };

    private OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            getRecords();
        }
    };

    private void getRecords() {
        AssetsRecordBody body = new AssetsRecordBody();
        // 添加货币类型
        String coinName = tvAssetName.getText().toString();
        String coinType = "";
        for (int i = 0; i < assets.size(); ++i) {
            if (assets.get(i).name.equals(coinName)) {
                coinType = assets.get(i).currencyId;
            }
        }

        if (!TextUtils.isEmpty(coinType)) {
            body.assets.put(coinName, Integer.valueOf(coinType));
        }

        // 添加操作类型
        String operName = tvOperName.getText().toString();
        if (operName.equals("类型")) {
            for (int i = 0; i < Constants.operTypes.size(); ++i) {
                body.opType.put(Constants.operTypes.get(i).name, Integer.valueOf(Constants.operTypes.get(i).id));
            }
        } else {
            body.opType.put(Constants.getOperName(curOperType), Integer.valueOf(curOperType));
        }

        body.recordsNo = "10";
        body.latestTranNo = latestTranNo;

        Map<String, String> signParams = new HashMap<>();
        signParams.put("assets", new Gson().toJson(body.assets));
        body.sig = SignUtil.generate(signParams, UserUtil.getSecret());

        new WalletDataManager()
                .getRecords(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomResult<RecordsEntity>() {
                    @Override
                    public void onError() {
                        lrvRecords.refreshComplete(0);
                    }

                    @Override
                    public void onSuccess(RecordsEntity result) {
                        lrvRecords.refreshComplete(0);

                        if (result.getData() == null) {
                            Logger.getLogger().e("获取资产记录, result为空");
                            return;
                        }

                        if (result.getData().records == null || result.getData().records.size() == 0) {
                            if (ActivityUtil.isAvailable(AssetRecordActivity.this)) {
                                AFToast.showShort(AssetRecordActivity.this, R.string.no_more_data);
                            }
                            return;
                        }

                        if (latestTranNo == null) {
                            recordAdapter.addAll(result.getData().records);
                        } else {
                            recordAdapter.add(result.getData().records);
                        }
                        latestTranNo = result.getData().records.get(result.getData().records.size() - 1).transNo;
                    }
                });
    }

    /**
     * 改变币种状态
     *
     * @param isClicked
     */
    public void changeAssetStatus(boolean isClicked) {
        if (isClicked) {
            tvAssetName.setTextColor(getResources().getColor(R.color.blue));
            ivAssetType.setImageResource(R.drawable.arrow_up);
        } else {
            tvAssetName.setTextColor(getResources().getColor(R.color.gray_9f));
            ivAssetType.setImageResource(R.drawable.arrow_down);
        }
    }

    /**
     * 改变操作类型状态
     *
     * @param isClicked
     */
    public void changeOperStatus(boolean isClicked) {
        if (isClicked) {
            tvOperName.setTextColor(getResources().getColor(R.color.blue));
            ivOperType.setImageResource(R.drawable.arrow_up);
        } else {
            tvOperName.setTextColor(getResources().getColor(R.color.gray_9f));
            ivOperType.setImageResource(R.drawable.arrow_down);
        }
    }

    private void addAssetsOptions() {
        llyOptions.removeAllViews();
        curType = "01";
        options.clear();
        tmpType = curCoinType;

        for (int i = 0; i < assets.size(); i = i + 3) {
            LinearLayout linearLayout = new LinearLayout(this);
            addLinearLayout(linearLayout);

            addTextView(linearLayout, assets.get(i).name, assets.get(i).currencyId, View.VISIBLE);
            addView(linearLayout);

            if (i + 1 < assets.size()) {
                addTextView(linearLayout, assets.get(i + 1).name, assets.get(i + 1).currencyId, View.VISIBLE);
            }
            else {
                addTextView(linearLayout, "", "", View.INVISIBLE);
            }

            addView(linearLayout);

            if (i + 2 < assets.size()) {
                addTextView(linearLayout, assets.get(i + 2).name, assets.get(i + 2).currencyId, View.VISIBLE);
            }
            else {
                addTextView(linearLayout, "", "", View.INVISIBLE);
            }

            llyOptions.addView(linearLayout);
        }
    }

    private void addOperTypesOptions() {
        llyOptions.removeAllViews();
        curType = "02";
        options.clear();
        tmpType = curOperType;

        for (int i = 0; i < operTypes.size(); i = i + 3) {
            LinearLayout linearLayout = new LinearLayout(this);
            addLinearLayout(linearLayout);

            addTextView(linearLayout, operTypes.get(i).name, operTypes.get(i).id, View.VISIBLE);
            addView(linearLayout);

            if (i + 1 < operTypes.size()) {
                addTextView(linearLayout, operTypes.get(i + 1).name, operTypes.get(i + 1).id, View.VISIBLE);
            }
            else {
                addTextView(linearLayout, "", "", View.INVISIBLE);
            }

            addView(linearLayout);

            if (i + 2 < operTypes.size()) {
                addTextView(linearLayout, operTypes.get(i + 2).name, operTypes.get(i + 2).id, View.VISIBLE);
            }
            else {
                addTextView(linearLayout, "", "", View.INVISIBLE);
            }

            llyOptions.addView(linearLayout);
        }
    }

    private void addLinearLayout(LinearLayout linearLayout) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(AutoUtils.getPercentWidthSize(15), 0, AutoUtils.getPercentWidthSize(15), 0);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setPadding(0, 0, 0, AutoUtils.getPercentHeightSize(15));
    }

    private void addTextView(LinearLayout root, final String text, final String id, int visible) {
        final TextView textView = new FontTextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(107), AutoUtils.getPercentHeightSize(36));
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        if (curType.equals("01") && curCoinType.equals(id) || curType.equals("02") && curOperType.equals(id)) {
            textView.setTextColor(getResources().getColor(R.color.blue));
            textView.setBackgroundResource(R.drawable.shape_rectangle_corner_blue);
        } else {
            textView.setBackgroundColor(getResources().getColor(R.color.gray_f5));
            textView.setTextColor(getResources().getColor(R.color.black_33));
        }
        textView.setTextSize(14);
        textView.setText(text);
        textView.setVisibility(visible);
        root.addView(textView);

        options.add(textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tmpType.equals(id) && curType.equals("02")) {
                    textView.setBackgroundColor(getResources().getColor(R.color.gray_f5));
                    textView.setTextColor(getResources().getColor(R.color.black_33));
                    tmpType = "";
                } else {
                    for (int i = 0; i < options.size(); ++i) {
                        TextView child = options.get(i);
                        if (child != null) {
                            child.setBackgroundColor(getResources().getColor(R.color.gray_f5));
                            child.setTextColor(getResources().getColor(R.color.black_33));
                        }
                    }
                    textView.setTextColor(getResources().getColor(R.color.blue));
                    textView.setBackgroundResource(R.drawable.shape_rectangle_corner_blue);
                    tmpType = id;
                }
            }
        });
    }

    private void addView(LinearLayout root) {
        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        root.addView(view);
    }

}

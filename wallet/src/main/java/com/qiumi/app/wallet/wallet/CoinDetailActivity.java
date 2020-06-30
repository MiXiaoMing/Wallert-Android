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

import com.appframe.library.component.image.ImageLoader;
import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.logger.Logger;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.qiumi.app.wallet.wallet.api.output.Record;
import com.qiumi.app.wallet.wallet.api.output.RecordsEntity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 货币详情
 */
public class CoinDetailActivity extends AutoBaseActivity {

    private TextView tvTitle, tvAmount, tvAmountRMB;
    private ImageView ivIcon;

    private TextView tvChoices;
    private LinearLayout llyOptionLayout, llyOptions;

    private LRecyclerView lrvRecords;
    private RecordAdapter recordAdapter;
    private String latestTranNo = null;

    private String curCoinType = "", curCoinName = "", curOperType = "", tmpOperType = "";
    private ArrayList<TextView> options = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail);

        curCoinType = getIntent().getStringExtra("coin");
        curCoinName = getIntent().getStringExtra("coinName");

        initView();
    }

    private void initView() {
        RelativeLayout rlyBack = findViewById(R.id.rlyBack);

        tvTitle = findViewById(R.id.tvTitle);
        tvAmount = findViewById(R.id.tvAmount);
        tvAmountRMB = findViewById(R.id.tvAmountRMB);
        ivIcon = findViewById(R.id.ivIcon);

        TextView tvWithdraw = findViewById(R.id.tvWithdraw);
        TextView tvDeposite = findViewById(R.id.tvDeposite);
        TextView tvTransferOut = findViewById(R.id.tvTransferOut);
        TextView tvTransferOutScan = findViewById(R.id.tvTransferOutScan);

        LinearLayout llyShowOption = findViewById(R.id.llyShowOption);
        tvChoices = findViewById(R.id.tvChoices);

        llyOptionLayout = findViewById(R.id.llyOptionLayout);
        View viewCancel = findViewById(R.id.viewCancel);
        TextView tvSure = findViewById(R.id.tvSure);
        llyOptions = findViewById(R.id.llyOptions);

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
        tvWithdraw.setOnClickListener(clickListener);
        tvDeposite.setOnClickListener(clickListener);
        llyShowOption.setOnClickListener(clickListener);
        tvTransferOut.setOnClickListener(clickListener);
        tvTransferOutScan.setOnClickListener(clickListener);
        llyOptionLayout.setOnClickListener(clickListener);
        viewCancel.setOnClickListener(clickListener);
        tvSure.setOnClickListener(clickListener);
    }

    private void initData(RecordsEntity entity) {
        tvTitle.setText(entity.assets.get(0).name);
        tvAmount.setText(entity.assets.get(0).price);
        tvAmountRMB.setText("≈ ￥" + entity.assets.get(0).priceTrans);
        ImageLoader.circle(this, entity.assets.get(0).icon, ivIcon);

        if (entity.records == null || entity.records.size() <= 0) {
            return;
        }

        if (TextUtils.isEmpty(latestTranNo)) {
            recordAdapter.addAll(entity.records);
        } else {
            recordAdapter.add(entity.records);
        }

        latestTranNo = entity.records.get(entity.records.size() - 1).transNo;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.rlyBack) {
                finish();
            } else if (viewId == R.id.tvWithdraw) {
                if (TextUtils.isEmpty(curCoinType) || TextUtils.isEmpty(tvTitle.getText().toString().trim())) {
                    return;
                }
                Intent intent = new Intent(CoinDetailActivity.this, WithDrawActivity.class);
                intent.putExtra("currencyId", curCoinType);
                intent.putExtra("name", tvTitle.getText().toString().trim());
                startActivity(intent);

            } else if (viewId == R.id.tvDeposite) {
                if (TextUtils.isEmpty(curCoinType) || TextUtils.isEmpty(tvTitle.getText().toString().trim())) {
                    return;
                }
                Intent intent = new Intent(CoinDetailActivity.this, DepositeActivity.class);
                intent.putExtra("currencyId", curCoinType);
                intent.putExtra("name", tvTitle.getText().toString().trim());
                startActivity(intent);
            } else if (viewId == R.id.llyShowOption) {
                if (llyOptionLayout.getVisibility() == View.VISIBLE) {
                    llyOptionLayout.setVisibility(View.GONE);
                } else {
                    llyOptionLayout.setVisibility(View.VISIBLE);
                    addOperTypesOptions();
                }
            } else if (viewId == R.id.tvTransferOut) {
                if (TextUtils.isEmpty(curCoinType) || TextUtils.isEmpty(tvTitle.getText().toString().trim())) {
                    return;
                }
                Intent intent = new Intent(CoinDetailActivity.this, TransferOutActivity.class);
                intent.putExtra("currencyId", curCoinType);
                intent.putExtra("name", tvTitle.getText().toString().trim());
                startActivity(intent);
            }  else if (viewId == R.id.tvTransferOutScan) {
                if (TextUtils.isEmpty(curCoinType) || TextUtils.isEmpty(tvTitle.getText().toString().trim())) {
                    return;
                }
                Intent intent = new Intent(CoinDetailActivity.this, TransferOutScanActivity.class);
                intent.putExtra("currencyId", curCoinType);
                intent.putExtra("name", tvTitle.getText().toString().trim());
                startActivity(intent);
            } else if (viewId == R.id.viewCancel) {
                llyOptionLayout.setVisibility(View.GONE);
            } else if (viewId == R.id.tvSure) {
                llyOptionLayout.setVisibility(View.GONE);
                if (!tmpOperType.equals(curOperType)) {
                    curOperType = tmpOperType;
                    if (!TextUtils.isEmpty(tmpOperType)) {
                        tvChoices.setText(Constants.getOperName(curOperType));
                    } else {
                        tvChoices.setText(getResources().getString(R.string.choices));
                    }
                    // 重新刷新数据
                    latestTranNo = null;
                    lrvRecords.refresh();
                }
            } else if (viewId == R.id.llyOptionLayout) {
                // 什么都不做，只是为了拦截点击事件
            }
        }
    };

    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Record record = recordAdapter.getItem(position);
            if (null != record) {
                Intent intent = new Intent(CoinDetailActivity.this, RecordDetailActivity.class);
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


    /******  网络请求  *******/

    private void getRecords() {
        AssetsRecordBody body = new AssetsRecordBody();
        body.assets.put(curCoinName, Integer.valueOf(curCoinType));
        if (TextUtils.isEmpty(curOperType)) {
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

                        if (result.getData() == null || result.getData().assets == null || result.getData().assets.size() <= 0) {
                            if (ActivityUtil.isAvailable(CoinDetailActivity.this)) {
                                AFToast.showShort(CoinDetailActivity.this, R.string.netword_fail);
                            }
                            return;
                        }

                        initData(result.getData());
                    }
                });
    }


    /******  动态添加布局  *******/

    private void addOperTypesOptions() {
        llyOptions.removeAllViews();
        options.clear();

        for (int i = 0; i < Constants.operTypes.size(); i = i + 3) {
            LinearLayout linearLayout = new LinearLayout(this);
            addLinearLayout(linearLayout);

            addTextView(linearLayout, Constants.operTypes.get(i).name, Constants.operTypes.get(i).id, View.VISIBLE);
            addView(linearLayout);

            if (i + 1 < Constants.operTypes.size()) {
                addTextView(linearLayout, Constants.operTypes.get(i + 1).name, Constants.operTypes.get(i + 1).id, View.VISIBLE);
            } else {
                addTextView(linearLayout, "", "", View.INVISIBLE);
            }

            addView(linearLayout);

            if (i + 2 < Constants.operTypes.size()) {
                addTextView(linearLayout, Constants.operTypes.get(i + 2).name, Constants.operTypes.get(i + 2).id, View.VISIBLE);
            } else {
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

    private void addTextView(final LinearLayout root, final String text, final String id, int visible) {
        final TextView textView = new FontTextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(107), AutoUtils.getPercentHeightSize(36));
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        if (curOperType.equals(id)) {
            textView.setTextColor(getResources().getColor(R.color.blue));
            textView.setBackgroundResource(R.drawable.shape_rectangle_corner_blue);
            tmpOperType = id;
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
                if (tmpOperType.equals(id)) {
                    textView.setBackgroundColor(getResources().getColor(R.color.gray_f5));
                    textView.setTextColor(getResources().getColor(R.color.black_33));
                    tmpOperType = "";
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
                    tmpOperType = id;
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

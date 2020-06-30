package com.qiumi.app.wallet.mine.identification;

import android.content.Context;

import com.qiumi.app.wallet.mine.identification.bean.MineCountryBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 提供国家列表数据。
 *
 * @author jiangkun
 * @date 2019/9/10
 */

public class MineCountryMocker {

    /**
     * 获取所有国家。
     *
     * @param context context
     * @return 所有国家
     */
    public static ArrayList<MineCountryBean> getAllCountries(Context context) {
        ArrayList<MineCountryBean> list = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("country.json")));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            JSONArray array = new JSONArray(builder.toString());
            String key = getKey(context);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                int code = object.getInt("code");
                String name = object.getString(key);
                MineCountryBean bean = new MineCountryBean(code, name);
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getKey(Context ctx) {
        try {
            String country = ctx.getResources().getConfiguration().locale.getCountry();
            return "CN".equalsIgnoreCase(country)? "zh"
                    : "TW".equalsIgnoreCase(country)? "tw"
                    : "HK".equalsIgnoreCase(country)? "tw"
                    : "en";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

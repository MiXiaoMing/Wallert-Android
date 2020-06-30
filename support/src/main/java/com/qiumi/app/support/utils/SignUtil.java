package com.qiumi.app.support.utils;

import com.appframe.utils.data.encoded.Hex;
import com.appframe.utils.logger.Logger;
import com.qiumi.app.support.Constants;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 签名工具
 */
public class SignUtil {

    public static String generate(Map<String, String> data, String append) {
        if (data == null || data.size() <= 0) {
            Logger.getLogger().e("签名数据错误");
            return "";
        }

        Set<String> keysSet = data.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);

        boolean first = true;
        StringBuilder str = new StringBuilder();
        for (Object key : keys) {
            if (!first) {
                str.append("&");
            } else {
                first = false;
            }
            str.append(key).append("=").append(data.get(key));
        }

        // 再加上 字符串
        str.append(append);
        Logger.getLogger().d("需加密字符串：" + str);

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA256");
            sha.update(str.toString().getBytes("UTF-8"));

            return Hex.encode(sha.digest()).toUpperCase();
        } catch (Exception e) {
            Logger.getLogger().e("签名异常：\n" + str + "\n" + e.toString());
            return "";
        }
    }
}

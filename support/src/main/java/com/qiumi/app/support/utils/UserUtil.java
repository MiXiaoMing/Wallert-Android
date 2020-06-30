package com.qiumi.app.support.utils;

import android.text.TextUtils;

import com.appframe.library.storage.SharePreferences;

public class UserUtil {
    // TODO: 2019/8/19 需要优化，先取内存数据，再取sp
    private static String key_token = "token";
    private static String key_uuid = "uuid";
    private static String key_secret = "secret";
    private static String key_secret_expire = "secretExpire";

    private static String key_userID = "userId";
    private static String key_userName = "userName";
    private static String key_avatar = "avatar";
    private static String key_telephone = "telephone";
    private static String key_telephone_full = "telephoneFull";
    private static String key_country_code = "countryCode";
    private static String key_total_usdt = "totalUsdt";

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getToken());
    }

    public class Secret {
        public String secret;
        public long expire;
    }

    public static void clear() {
        setToken("");
        setUUID("");
        setSecret("");
        setSecretExpire(Long.MAX_VALUE);

        setUserID("");
        setUserName("");
        setAvatar("");
        setTelephone("");
        setTotalUSDT("");
    }

    public static void setToken(String id) {
        SharePreferences.putString(key_token, id);
    }

    public static String getToken() {
        return SharePreferences.getString(key_token);
    }

    public static void setUUID(String uuid) {
        SharePreferences.putString(key_uuid, uuid);
    }

    public static String getUUID() {
        return SharePreferences.getString(key_uuid);
    }

    public static void setSecret(String secret) {
        SharePreferences.putString(key_secret, secret);
    }

    public static long getSecretExpire() {
        return SharePreferences.getLong(key_secret_expire, Long.MAX_VALUE);
    }

    public static void setSecretExpire(long secretExpire) {
        SharePreferences.putLong(key_secret_expire, secretExpire);
    }

    public static String getSecret() {
        return SharePreferences.getString(key_secret);
    }

    public static void setUserID(String id) {
        SharePreferences.putString(key_userID, id);
    }

    public static String getUserID() {
        return SharePreferences.getString(key_userID);
    }

    public static void setUserName(String userName) {
        SharePreferences.putString(key_userName, userName);
    }

    public static String getUserName() {
        return SharePreferences.getString(key_userName);
    }

    public static void setAvatar(String avatar) {
        SharePreferences.putString(key_avatar, avatar);
    }

    public static String getAvatar() {
        return SharePreferences.getString(key_avatar);
    }

    public static void setTelephone(String telephone) {
        SharePreferences.putString(key_telephone, telephone);
    }

    public static String getTelephone() {
        return SharePreferences.getString(key_telephone);
    }

    public static void setTelephoneFull(String telephone) {
        SharePreferences.putString(key_telephone_full, telephone);
    }

    public static String getTelephoneFull() {
        return SharePreferences.getString(key_telephone_full);
    }

    public static void setCountryCode(String countryCode) {
        SharePreferences.putString(key_country_code, countryCode);
    }

    public static String getCountryCode() {
        return SharePreferences.getString(key_country_code);
    }

    public static String getTotalUSDT() {
        return SharePreferences.getString(key_total_usdt);
    }

    public static void setTotalUSDT(String totalUSDT) {
        SharePreferences.putString(key_total_usdt, totalUSDT);
    }
}

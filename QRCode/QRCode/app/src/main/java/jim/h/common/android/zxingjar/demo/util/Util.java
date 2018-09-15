package jim.h.common.android.zxingjar.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import jim.h.common.android.zxingjar.demo.constants.RestConstants;

/**
 * Created by NiRRaNjAN on 22/01/17.
 */

public class Util implements RestConstants {

    public static String getUrl(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = preferences.getString(IP, "192.168.1.101");
        String port = preferences.getString(PORT, "8080");
        return "http://" + ip + ":" + port + WEB_SERVICES;
    }

    public static String getIp(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(IP, "192.168.1.101");
    }

    public static String getPort(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PORT, "8080");
    }

    public static void addToPreferences(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

}
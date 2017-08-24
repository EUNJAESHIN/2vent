package com.example.win.a2vent.util;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by win on 2017-07-24.
 */

public class GlobalData {
    private final static String URL = "http://121.151.63.53:8080/EventApp/";
    public final static String OWNER_MAIN_RECEIVER = "com.example.win.a2vent.OwnerEventMainReceiver";
    public final static String GET_URI_RECEIVER = "com.example.win.a2vent.GetURI_Receiver";
    public final static String ADD_EVENT_RECEIVER = "com.example.win.a2vent.AddEventReceiver";
    public final static String USER_MAP_RECEIVER = "com.example.win.a2vent.UserMapReceiver";
    public final static String USER_GPS_RECEIVER = "com.example.win.a2vent.UserGpsReceiver";
    public final static String USER_MAP_SERVICE = "com.example.win.a2vent.SERVICE_GPS_INFO";
    private static String UserID, UserPW, UserAccountNum,
            UserName, UserBirth, UserAddr, UserSex, UserPhone;

    public static String getURL() {
        return URL;
    }

    public static String getUserID() {
        return UserID;
    }

    public static String getUserPW() {
        return UserPW;
    }

    public static String getUserName() {
        return UserName;
    }

    public static String getUserAddr() {
        return UserAddr;
    }

    public static String getUserBirth() {
        return UserBirth;
    }

    public static String getUserSex() {
        return UserSex;
    }

    public static String getUserPhone() {
        return UserPhone;
    }

    public static String getUserAccountNum() {
        return UserAccountNum;
    }

    public static void setUserData(String id, String pw, String name, String addr, String birth,
                                   String sex, String phone, String acc) {
        UserID = id;
        UserPW = pw;
        UserName = name;
        UserAddr = addr;
        UserBirth = birth;
        UserSex = sex;
        UserPhone = phone;
        UserAccountNum = acc;
    }

    public static void hideKeyboard(InputMethodManager imm, View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static String setSHA256(String str) {
        String SHA = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }

}

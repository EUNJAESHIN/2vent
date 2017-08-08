package com.example.win.a2vent;

import java.security.MessageDigest;

/**
 * Created by win on 2017-07-24.
 */

public class GlobalData {
    private final static String URL = "http://121.151.63.53:8080/EventApp/";
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

    public static String getEncMD5(String txt) throws Exception {
        StringBuffer sbuf = new StringBuffer();

        MessageDigest mDigest = MessageDigest.getInstance("MD5");
        mDigest.update(txt.getBytes());

        byte[] msgStr = mDigest.digest();

        for (int i = 0; i < msgStr.length; i++) {
            String tmpEncTxt = Integer.toHexString((int) msgStr[i] & 0x00ff);
            sbuf.append(tmpEncTxt);
        }

        return sbuf.toString();
    }

}

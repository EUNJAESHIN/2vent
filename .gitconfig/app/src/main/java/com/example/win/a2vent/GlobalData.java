package com.example.win.a2vent;

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

    public static void setUserData(String id,String pw, String name, String addr, String birth,
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

}

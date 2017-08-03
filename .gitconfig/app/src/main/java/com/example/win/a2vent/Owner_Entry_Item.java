package com.example.win.a2vent;

/**
 * Created by win on 2017-08-01.
 */

public class Owner_Entry_Item {
    private int mNo;
    private String mID;
    private String mName;
    private String mAddress;
    private String mSex;
    private String mPhoneNo;
    private String mIs_entry;

    public Owner_Entry_Item(int no, String id, String name, String address, String sex, String phoneNo, String is_entry) {
        mNo = no;
        mID = id;
        mName = name;
        mAddress = address;
        mSex = sex;
        mPhoneNo = phoneNo;
        mIs_entry = is_entry;
    }

    public String getNo() {
        return String.valueOf(mNo);
    }

    public String getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getSex() {
        return mSex;
    }

    public String getPhoneNo() {
        return mPhoneNo;
    }

    public String getIs_entry() {
        return mIs_entry;
    }


}

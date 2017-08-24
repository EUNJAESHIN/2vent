package com.example.win.a2vent.onwer.main;

/**
 * Created by win on 2017-07-03.
 */

public class Owner_Store_Item {

    private String mCom_number;
    private String mCom_name;
    private String mCom_addr;
    private String mCom_detail_addr;
    private String mCom_category;
    private String mCom_manager;
    private String mCom_URI;
    private String mId;

    public Owner_Store_Item(String com_number, String com_name, String com_addr, String com_detail_addr, String com_category, String com_manager, String com_URI, String id) {
        mCom_number = com_number;
        mCom_name = com_name;
        mCom_addr = com_addr;
        mCom_detail_addr = com_detail_addr;
        mCom_category = com_category;
        mCom_manager = com_manager;
        mCom_URI = com_URI;
        mId = id;
    }

    public String getCom_number() {
        return mCom_number;
    }

    public String getCom_name() {
        return mCom_name;
    }

    public String getCom_addr() {
        return mCom_addr;
    }

    public String getCom_detail_addr() {
        return mCom_detail_addr;
    }

    public String getCom_category() {
        return mCom_category;
    }

    public String getCom_manager() {
        return mCom_manager;
    }

    public String getCom_URI() {
        return mCom_URI;
    }

    public String getId() {
        return mId;
    }

}

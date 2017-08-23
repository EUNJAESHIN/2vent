package com.example.win.a2vent.onwer.main;

/**
 * Created by win on 2017-07-25.
 */

public class Owner_Event_Simple_Item {
    private String mEvent_number;
    private String mEvent_name;
    private String mEvent_type;
    private String mEvent_price;
    private String mEvent_dis_price;
    private String mEvent_startday;
    private String mEvent_endday;
    private String mCom_name;
    private String mEvent_URI;

    public Owner_Event_Simple_Item(String event_number, String event_name, String event_type,String event_price,
                                   String event_dis_price, String event_startday, String event_endday, String com_name, String event_URI) {
        mEvent_number = event_number;
        mEvent_name = event_name;
        mEvent_type = event_type;
        mEvent_price = event_price;
        mEvent_dis_price = event_dis_price;
        mEvent_startday = event_startday;
        mEvent_endday = event_endday;
        mCom_name = com_name;
        mEvent_URI = event_URI;
    }

    public String getEvent_number() { return mEvent_number; }

    public String getEvent_name() {
        return mEvent_name;
    }

    public String getEvent_type() {
        return mEvent_type;
    }

    public String getEvent_URI() {
        return mEvent_URI;
    }

    public String getEvent_price() {
        return mEvent_price;
    }

    public String getEvent_dis_price() {
        return mEvent_dis_price;
    }

    public String getEvent_startday() {
        return mEvent_startday;
    }

    public String getEvent_endday() {
        return mEvent_endday;
    }

    public String getCom_name() {
        return mCom_name;
    }
}

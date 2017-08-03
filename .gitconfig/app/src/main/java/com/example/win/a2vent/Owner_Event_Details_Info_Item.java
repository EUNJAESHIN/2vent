package com.example.win.a2vent;

/**
 * Created by win on 2017-07-27.
 */

public class Owner_Event_Details_Info_Item {
    private String mEvent_name, mEvent_type, mEvent_stats, mEvent_URI, mEvent_price, mEvent_dis_price, mEvent_people, mEvent_startday, mEvent_endday, mEvent_startime, mEvent_endtime, mEvent_payment, mEvent_target, mEvent_minage, mEvent_maxage, mEvent_sex, mEvent_area, mCom_name;

    public Owner_Event_Details_Info_Item(String event_name, String event_type, String event_stats,
                                         String event_URI, String event_price, String event_dis_price, String event_people,
                                         String event_startday, String event_endday, String event_starttime, String event_endtime,
                                         String event_payment, String event_target, String event_minage, String event_maxage,
                                         String event_sex, String event_area, String com_name) {
        mEvent_name = event_name;
        mEvent_type = event_type;
        mEvent_stats = event_stats;
        mEvent_URI = event_URI;
        mEvent_price = event_price;
        mEvent_dis_price = event_dis_price;
        mEvent_people = event_people;
        mEvent_startday = event_startday;
        mEvent_endday = event_endday;
        mEvent_startime = event_starttime;
        mEvent_endtime = event_endtime;
        mEvent_payment = event_payment;
        mEvent_target = event_target;
        mEvent_minage = event_minage;
        mEvent_maxage = event_maxage;
        mEvent_sex = event_sex;
        mEvent_area = event_area;
        mCom_name = com_name;
    }


    public String getEvent_name() {
        return mEvent_name;
    }

    public String getEvent_type() {
        return mEvent_type;
    }

    public String getEvent_stats() {
        return mEvent_stats;
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

    public String getEvent_people() {
        return mEvent_people;
    }

    public String getEvent_startday() {
        return mEvent_startday;
    }

    public String getEvent_endday() {
        return mEvent_endday;
    }

    public String getEvent_starttime() {
        return mEvent_startime;
    }

    public String getEvent_endtime() {
        return mEvent_endtime;
    }

    public String getEvent_payment() {
        return mEvent_payment;
    }

    public String getEvent_target() {
        return mEvent_target;
    }

    public String getEvent_minage() {
        return mEvent_minage;
    }

    public String getEvent_maxage() {
        return mEvent_maxage;
    }

    public String getEvent_sex() {
        return mEvent_sex;
    }

    public String getEvent_area() {
        return mEvent_area;
    }

    public String getCom_name() {
        return mCom_name;
    }
}

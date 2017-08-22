package com.example.win.a2vent.onwer.add_store;

/**
 * Created by win on 2017-08-01.
 */

public class Owner_Store_Event_List_Item {
    private String mEvent_number;
    private String mEvent_name;
    private String mEvent_URI;
    private String mEvent_startday;
    private String mEvent_endday;

    public Owner_Store_Event_List_Item(String event_number, String event_name, String event_startday, String event_endday, String event_URI) {
        mEvent_number = event_number;
        mEvent_name = event_name;
        mEvent_URI = event_URI;
        mEvent_startday = event_startday;
        mEvent_endday = event_endday;
    }

    public String getEvent_number() {
        return mEvent_number;
    }

    public String getEvent_name() {
        return mEvent_name;
    }

    public String getEvent_URI() {
        return mEvent_URI;
    }

    public String getEvent_startday() {
        return mEvent_startday;
    }

    public String getEvent_endday() {
        return mEvent_endday;
    }
}

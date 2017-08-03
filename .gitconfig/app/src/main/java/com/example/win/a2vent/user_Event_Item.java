package com.example.win.a2vent;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 뷰의 아이템
 */

public class User_Event_Item {

    int event_number;
    String event_name, event_URI;
    String event_price, event_dis_price;
    String event_startday, event_endday;

    public int getEventNum() { return event_number; }

    public String getEventName() { return event_name; }

    public String getEventURI() { return event_URI; }

    public String getEventPrice() { return event_price; }

    public String getEventDisprice() { return event_dis_price; }

    public String getEventStartday() { return event_startday; }

    public String getEventEndday() { return event_endday; }

    public User_Event_Item(int num, String name, String uri, String price, String dis_price,
                           String startday, String endday) {
        this.event_number = num;
        this.event_name = name;
        this.event_URI = uri;
        this.event_price = price;
        this.event_dis_price = dis_price;
        this.event_startday = startday;
        this.event_endday = endday;
    }
}

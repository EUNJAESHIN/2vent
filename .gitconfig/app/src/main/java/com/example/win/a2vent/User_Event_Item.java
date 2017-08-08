package com.example.win.a2vent;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 뷰의 아이템
 */

public class User_Event_Item {

    int event_number;
    String event_name, event_content;
    String event_price, event_dis_price;
    String event_startday, event_endday;
    String event_URI;

    public int getEventNum() { return event_number; }

    public String getEventName() { return event_name; }

    public String getEventContent() { return event_content; }

    public String getEventPrice() { return event_price; }

    public String getEventDisprice() { return event_dis_price; }

    public String getEventStartday() { return event_startday; }

    public String getEventEndday() { return event_endday; }

    public String getEventURI() { return event_URI; }

    public User_Event_Item(int num, String name, String content, String price, String dis_price,
                           String startday, String endday, String uri) {
        this.event_number = num;
        this.event_name = name;
        this.event_content = content;
        this.event_price = price;
        this.event_dis_price = dis_price;
        this.event_startday = startday;
        this.event_endday = endday;
        this.event_URI = uri;
    }

}

package com.example.win.a2vent.user.map;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by win on 2017-08-18.
 */

public class SimpleInfoListItem {
    private int mEventNumber;
    private String mEventName, mEventPrice, mEventDisPrice, mEventEndDay, mEventEndTime, mEventEndDate, mEventURI, mComName, mComAddress;

    public SimpleInfoListItem(int event_number, String event_name, String event_price, String event_dis_price,
                              String event_endday, String event_endtime, String event_URI,
                              String com_name, String com_addr) {
        mEventNumber = event_number;
        mEventName = event_name;
        mEventPrice = event_price;
        mEventDisPrice = event_dis_price;
        mEventEndDay = event_endday;
        mEventEndTime = event_endtime;
        mEventURI = event_URI;
        mComName = com_name;
        mComAddress = com_addr;
    }

    public String getEventPrice() {
        return mEventPrice;
    }

    public String getEventDisPrice() {
        return mEventDisPrice;
    }

    public String getEventURI() {
        return mEventURI;
    }

    public String getEventName() {
        return mEventName;
    }

    public String getEventEndDate() {
        StringTokenizer token = new StringTokenizer(mEventEndDay, "-");
        String year = null, month = null, day = null;

        try {
            year = token.nextToken();
            month = token.nextToken();
            day = token.nextToken();
        } catch (NoSuchElementException e) {

        }

        token = new StringTokenizer(mEventEndTime, ":");
        String hour = null, min = null;

        try {
            hour = token.nextToken();
            min = token.nextToken();
        } catch (NoSuchElementException e) {

        }

        mEventEndDate = year.concat("년 ").concat(month).concat("월").concat(day).concat("일  ")
                .concat(hour).concat(":").concat(min);

        return mEventEndDate;
    }

    public int getEventNumber() {
        return mEventNumber;
    }

    public String getComName() {
        return mComName;
    }

    public String getComAddress() {
        return mComAddress;
    }
}

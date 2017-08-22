package com.example.win.a2vent;

import android.graphics.Bitmap;

/**
 * Created by EUNJAESHIN on 2017-08-22.
 */

public class Owner_Review_Item {

    Bitmap event_IMG;

    public Bitmap getEventIMG() { return event_IMG; }

    public Owner_Review_Item(Bitmap img) {
        this.event_IMG = img;
    }

}
package com.example.win.a2vent;

import android.graphics.Bitmap;

/**
 * Created by win on 2017-08-08.
 */

public class Owner_Add_Event_Item {

    private String mFileName;
    private String URI;
    private Bitmap mBitmap;

    public Owner_Add_Event_Item(String fileName, String uri, Bitmap bitmap) {
        mFileName = fileName;
        URI = uri;
        mBitmap = bitmap;
    }

    public String getURI() {
        return URI;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getFileName() {
        return mFileName;
    }
}

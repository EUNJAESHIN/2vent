package com.example.win.a2vent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by win on 2017-08-08.
 */

public class Owner_Add_Event_Holder extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public Owner_Add_Event_Holder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }
}

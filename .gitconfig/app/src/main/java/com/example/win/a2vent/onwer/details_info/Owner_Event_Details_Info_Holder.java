package com.example.win.a2vent.onwer.details_info;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.win.a2vent.R;

/**
 * Created by win on 2017-08-08.
 */

public class Owner_Event_Details_Info_Holder extends RecyclerView.ViewHolder {

    private ImageView imageContents;

    public Owner_Event_Details_Info_Holder(View itemView) {
        super(itemView);
        imageContents = (ImageView) itemView.findViewById(R.id.imgContents);
    }

    public ImageView getImageContents() {
        return imageContents;
    }
}

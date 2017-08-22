package com.example.win.a2vent.user.details_info;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.win.a2vent.R;

/**
 * Created by EUNJAESHIN on 2017-08-08.
 */

public class User_Details_Holder extends RecyclerView.ViewHolder {

    public View view;
    public ImageView imageView;

    public User_Details_Holder(View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.user_details_cardview);
        imageView = (ImageView) itemView.findViewById(R.id.user_details_cardview_image);
    }

}

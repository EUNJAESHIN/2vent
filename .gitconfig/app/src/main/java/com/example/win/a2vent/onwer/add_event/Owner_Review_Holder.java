package com.example.win.a2vent.onwer.add_event;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.win.a2vent.R;

/**
 * Created by EUNJAESHIN on 2017-08-22.
 */

public class Owner_Review_Holder extends RecyclerView.ViewHolder {

    public View view;
    public ImageView imageView;

    public Owner_Review_Holder(View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.owner_review_cardview);
        imageView = (ImageView) itemView.findViewById(R.id.owner_review_cardview_image);
    }

}
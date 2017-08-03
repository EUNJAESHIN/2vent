package com.example.win.a2vent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by win on 2017-08-01.
 */

public class Owner_Store_Event_List_Holder extends RecyclerView.ViewHolder{

    private LinearLayout llList;
    private ImageView imageView;
    private TextView tvEventName;
    private TextView tvEventDate;

    public Owner_Store_Event_List_Holder(View itemView) {
        super(itemView);
        llList = (LinearLayout) itemView.findViewById(R.id.llList);
        imageView = (ImageView) itemView.findViewById(R.id.imgEvent);
        tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
        tvEventDate = (TextView) itemView.findViewById(R.id.tvEventDate);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTvEventName() {
        return tvEventName;
    }

    public TextView getTvEventDate() {
        return tvEventDate;
    }

    public LinearLayout getLlList() {
        return llList;
    }
}

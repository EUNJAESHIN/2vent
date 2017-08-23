package com.example.win.a2vent.onwer.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.win.a2vent.R;


/**
 * Created by win on 2017-07-05.
 */

public class Owner_Event_Simple_Holder extends RecyclerView.ViewHolder{
    private ImageView event_imageView;
    private TextView event_name;
    private TextView com_name;
    private TextView event_type;
    private TextView event_price;
    private TextView event_dis_price;
    private TextView event_startday;
    private TextView event_endday;

    public Owner_Event_Simple_Holder(View itemView) {
        super(itemView);
        event_imageView =(ImageView) itemView.findViewById(R.id.event_imageView);
        event_name=(TextView) itemView.findViewById(R.id.event_name);
        com_name=(TextView) itemView.findViewById(R.id.com_name);
        event_type=(TextView) itemView.findViewById(R.id.event_type);
        event_price=(TextView) itemView.findViewById(R.id.event_price);
        event_dis_price=(TextView) itemView.findViewById(R.id.event_dis_price);
        event_startday=(TextView) itemView.findViewById(R.id.event_startday);
        event_endday=(TextView) itemView.findViewById(R.id.event_endday);
    }

    public ImageView getEvent_imageView() { return event_imageView; }

    public TextView getEvent_name() { return event_name; }

    public TextView getCom_name() { return com_name; }

    public TextView getEvent_type() { return event_type; }

    public TextView getEvent_price() { return event_price; }

    public TextView getEvent_dis_price() { return event_dis_price; }

    public TextView getEvent_startday() { return event_startday; }

    public TextView getEvent_endday() { return event_endday; }
}

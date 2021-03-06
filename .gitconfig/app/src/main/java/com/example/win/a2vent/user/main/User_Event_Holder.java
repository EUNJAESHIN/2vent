package com.example.win.a2vent.user.main;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.win.a2vent.R;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 뷰홀더만
 */

public class User_Event_Holder extends RecyclerView.ViewHolder {

    public View view;
    public ImageView imageView;
    public TextView textView1,textView2,textView3,textView4;

    public User_Event_Holder(View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.user_event_cardview);
        imageView = (ImageView) itemView.findViewById(R.id.user_event_cardview_image);
        textView1 = (TextView) itemView.findViewById(R.id.user_event_cardview_text1);
        textView2 = (TextView) itemView.findViewById(R.id.user_event_cardview_text2);
        textView2.setPaintFlags(textView2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG); // 삭선
        textView3 = (TextView) itemView.findViewById(R.id.user_event_cardview_text3);
        textView4 = (TextView) itemView.findViewById(R.id.user_event_cardview_text4);
    }

}

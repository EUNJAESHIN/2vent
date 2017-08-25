package com.example.win.a2vent.user.entry_list;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.win.a2vent.R;

/**
 * Created by EUNJAESHIN on 2017-08-03.
 */

public class User_Entry_Holder extends RecyclerView.ViewHolder {

    public View view;
    public TextView textView1,textView2,textView3,textView4;

    public User_Entry_Holder(View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.user_entry_cardview);
        textView1 = (TextView) itemView.findViewById(R.id.user_entry_cardview_text1);
        textView2 = (TextView) itemView.findViewById(R.id.user_entry_cardview_text2);
        textView2.setPaintFlags(textView2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG); // 삭선
        textView3 = (TextView) itemView.findViewById(R.id.user_entry_cardview_text3);
        textView4 = (TextView) itemView.findViewById(R.id.user_entry_cardview_text4);

    }
}

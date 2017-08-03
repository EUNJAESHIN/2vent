package com.example.win.a2vent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by win on 2017-07-03.
 */

public class Owner_Store_Holder extends RecyclerView.ViewHolder {

    private ImageView mCom_imageView;
    private TextView mCom_name;
    private TextView mCom_addr;
    private TextView mCom_manager;
    private TextView mCom_category;
    private TextView mCom_number;


    public Owner_Store_Holder(View itemView) {
        super(itemView);
        mCom_imageView = (ImageView) itemView.findViewById(R.id.com_cardview_image);
        mCom_addr = (TextView) itemView.findViewById(R.id.com_cardview_text1);
        mCom_name  = (TextView) itemView.findViewById(R.id.com_cardview_text2);
        mCom_manager = (TextView) itemView.findViewById(R.id.com_cardview_text3);
        mCom_category = (TextView) itemView.findViewById(R.id.com_cardview_text4);
        mCom_number = (TextView) itemView.findViewById(R.id.com_cardview_text5);

    }

    public ImageView getCom_imageView() {
        return mCom_imageView;
    }

    public TextView getCom_name() {
        return mCom_name;
    }

    public TextView getCom_addr() {
        return mCom_addr;
    }

    public TextView getCom_manager() {
        return mCom_manager;
    }

    public TextView getCom_category() {
        return mCom_category;
    }

    public TextView getCom_number() {
        return mCom_number;
    }
}

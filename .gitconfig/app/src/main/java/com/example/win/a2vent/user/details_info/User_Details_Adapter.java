package com.example.win.a2vent.user.details_info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.win.a2vent.R;
import com.example.win.a2vent.util.GlobalData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by EUNJAESHIN on 2017-08-08.
 */

public class User_Details_Adapter extends RecyclerView.Adapter<User_Details_Holder> {

    private Context context;
    private ArrayList<User_Details_Item> mItems = new ArrayList<User_Details_Item>();

    public User_Details_Adapter(ArrayList items, Context mContext) {
        mItems = items;
        context = mContext;
    }

    @Override
    public User_Details_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_details_info_cardview, parent, false);
        User_Details_Holder holder = new User_Details_Holder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(User_Details_Holder holder, int position) {
        Picasso.with(context).load(GlobalData.getURL() + mItems.get(position).getEventURI()).
                placeholder(R.drawable.event_default).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

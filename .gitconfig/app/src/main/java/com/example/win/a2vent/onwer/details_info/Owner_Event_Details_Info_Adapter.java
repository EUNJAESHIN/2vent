package com.example.win.a2vent.onwer.details_info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.win.a2vent.R;
import com.example.win.a2vent.util.GlobalData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by win on 2017-08-08.
 */

public class Owner_Event_Details_Info_Adapter extends RecyclerView.Adapter<Owner_Event_Details_Info_Holder> {

    private final String TAG = "테스트";

    private ArrayList<Owner_Event_Details_Info_Item> mArrayList = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;

    public Owner_Event_Details_Info_Adapter(ArrayList<Owner_Event_Details_Info_Item> arrayList, Context context) {
        mArrayList = arrayList;
        mContext = context;
    }

    @Override
    public Owner_Event_Details_Info_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_event_details_info_item, parent, false);
        Owner_Event_Details_Info_Holder holder = new Owner_Event_Details_Info_Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Owner_Event_Details_Info_Holder holder, int position) {
        Picasso.with(mContext).load(GlobalData.getURL() + mArrayList.get(position).getEvent_URI()).placeholder(R.drawable.event_default)
                .into(holder.getImageContents());

        setAnimation(holder.getImageContents(), position);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

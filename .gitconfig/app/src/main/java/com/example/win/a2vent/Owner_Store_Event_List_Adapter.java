package com.example.win.a2vent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by win on 2017-08-01.
 */

public class Owner_Store_Event_List_Adapter extends RecyclerView.Adapter<Owner_Store_Event_List_Holder> {

    private String TAG = "테스트";

    private ArrayList<Owner_Store_Event_List_Item> mArrayList;
    private Context mContext;
    private int lastPosition = -1;

    public Owner_Store_Event_List_Adapter(ArrayList<Owner_Store_Event_List_Item> arrayList, Context context) {
        mArrayList = new ArrayList<>();
        mArrayList = arrayList;
        mContext = context;
    }

    @Override
    public Owner_Store_Event_List_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_store_event_list_item, parent, false);
        Owner_Store_Event_List_Holder holder = new Owner_Store_Event_List_Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Owner_Store_Event_List_Holder holder, final int position) {
        Picasso.with(mContext).load(GlobalData.getURL() + mArrayList.get(position).getEvent_URI()).placeholder(R.drawable.event_default)
                .into(holder.getImageView());
        holder.getTvEventName().setText(mArrayList.get(position).getEvent_name());
        holder.getTvEventDate().setText(mArrayList.get(position).getEvent_startday().concat(" ~ ").concat(mArrayList.get(position).getEvent_endday()));

        holder.getLlList().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "event number : " + mArrayList.get(position).getEvent_number());
                Intent intent = new Intent(mContext, Activity_Owner_Entry_List.class);
                intent.putExtra("event_number", mArrayList.get(position).getEvent_number());
                mContext.startActivity(intent);
            }
        });

        setAnimation(holder.getImageView(), position);
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

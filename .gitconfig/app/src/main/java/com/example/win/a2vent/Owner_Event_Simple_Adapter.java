package com.example.win.a2vent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by win on 2017-07-05.
 */

public class Owner_Event_Simple_Adapter extends RecyclerView.Adapter<Owner_Event_Simple_Holder> {

    private String TAG = "테스트";

    private ArrayList<Owner_Event_Simple_Item> mEvent_list = new ArrayList<>();
    private Context mContext;
    private int mEvent_stats;
    private int lastPosition = -1;

    public Owner_Event_Simple_Adapter(ArrayList<Owner_Event_Simple_Item> event_list, Context context, int event_stats) {
        mEvent_list = event_list;
        mContext = context;
        mEvent_stats = event_stats;
    }

    @Override
    public Owner_Event_Simple_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_event_cardview, parent, false);
        Owner_Event_Simple_Holder holder = new Owner_Event_Simple_Holder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(Owner_Event_Simple_Holder holder, final int position) {
        if ((mEvent_stats == 0) || (mEvent_stats == 2)) {
            Picasso.with(mContext).load(GlobalData.getURL() + mEvent_list.get(position).getEvent_URI()).placeholder(R.drawable.event_default)
                    .into(holder.getEvent_imageView());
        } else if (mEvent_stats == 1) {
            Picasso.with(mContext).load(GlobalData.getURL() + mEvent_list.get(position).getEvent_URI()).placeholder(R.drawable.event_default)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                    .into(holder.getEvent_imageView());
        }
        holder.getEvent_name().setText(mEvent_list.get(position).getEvent_name());
        holder.getCom_name().setText(mEvent_list.get(position).getCom_name());
        holder.getEvent_type().setText(mEvent_list.get(position).getEvent_type());
        holder.getEvent_price().setText(mEvent_list.get(position).getEvent_price());
        holder.getEvent_dis_price().setText(mEvent_list.get(position).getEvent_dis_price());
        holder.getEvent_startday().setText(mEvent_list.get(position).getEvent_startday());
        holder.getEvent_endday().setText(mEvent_list.get(position).getEvent_endday());

        setAnimation(holder.getEvent_imageView(), position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (mEvent_stats) {
                    case 0:
                        Log.d(TAG, "event number : " + mEvent_list.get(position).getEvent_number());
                        intent = new Intent(mContext, Activity_Owner_Event_Details_Info.class);
                        intent.putExtra("event_number", mEvent_list.get(position).getEvent_number());
                        mContext.startActivity(intent);
                        break;
                    case 1:
                        Log.d(TAG, "event number : " + mEvent_list.get(position).getEvent_number());
                        intent = new Intent(mContext, Activity_Owner_Add_Event.class);
                        intent.putExtra("event_number", mEvent_list.get(position).getEvent_number());
                        mContext.startActivity(intent);
                        break;
                    case 2:
                        Log.d(TAG, "event number : " + mEvent_list.get(position).getEvent_number());
                        intent = new Intent(mContext, Activity_Owner_Event_Details_Info.class);
                        intent.putExtra("event_number", mEvent_list.get(position).getEvent_number());
                        mContext.startActivity(intent);
                        break;
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                switch (mEvent_stats) {
                    case 1:
                    case 2:
                        PopupMenu popupMenu = new PopupMenu(mContext, v);
                        popupMenu.inflate(R.menu.popup_menu);
                        popupMenu.setGravity(Gravity.RIGHT);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("삭제 하시겠습니까?").setCancelable(false)
                                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DeleteData deleteData = new DeleteData(mContext, "EventKey", "event_number", mEvent_list.get(position).getEvent_number());
                                                deleteData.execute("2ventDeleteEvent.php");
                                            }
                                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                                return true;
                            }
                        });
                        popupMenu.show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvent_list.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

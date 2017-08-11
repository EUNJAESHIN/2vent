package com.example.win.a2vent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by EUNJAESHIN on 2017-08-03.
 */

public class User_Entry_Adapter extends RecyclerView.Adapter<User_Entry_Holder> {

    DeleteUserEntry deleteUserEntry;
    private Context context;
    private ArrayList<User_Entry_Item> mItems = new ArrayList<User_Entry_Item>();
    static int delete_event_number;
    AlertDialog.Builder builder_EntryDeleteAlert;

    public User_Entry_Adapter(ArrayList items, Context mContext) {
        mItems = items;
        context = mContext;
    }

    @Override
    public User_Entry_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_entry_cardview, parent, false);
        User_Entry_Holder holder = new User_Entry_Holder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(User_Entry_Holder holder, final int position) {
        holder.textView1.setText(mItems.get(position).getEventName() +
                "  (" + mItems.get(position).getEventCategory() + ")");
        holder.textView2.setText("정상가 : " + mItems.get(position).getEventPrice());
        holder.textView3.setText("할인가 : " + mItems.get(position).getEventDisprice());
        holder.textView4.setText(mItems.get(position).getEventStartday() +
                "  ~  " + mItems.get(position).getEventEndday());

//        아이템 클릭리스너 (RecyclerItemClickListener)
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_goDetail = new Intent(context, Activity_User_Event_Details_Info.class);
                intent_goDetail.putExtra("event_number", mItems.get(position).getEventNum());
                // Intent에 event_number값을 담기
                context.startActivity(intent_goDetail);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                delete_event_number = mItems.get(position).getEventNum();
                deleteUserEntry = new DeleteUserEntry();
                deleteUserEntry.
                        execute(Integer.toString(mItems.get(position).getEventNum()), GlobalData.getUserID());
                // 새로고침
                Intent intent_Refresh = new Intent(context, Activity_User_Entry_List.class);
                intent_Refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent_Refresh);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
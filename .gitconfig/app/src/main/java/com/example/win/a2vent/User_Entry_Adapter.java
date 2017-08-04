package com.example.win.a2vent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.win.a2vent.Activity_User_Login.toast;

/**
 * Created by EUNJAESHIN on 2017-08-03.
 */

public class User_Entry_Adapter extends RecyclerView.Adapter<User_Entry_Holder> {

    DeleteUserEntry deleteUserEntry;
    private Context context;
    private ArrayList<User_Entry_Item> mItems = new ArrayList<User_Entry_Item>();
    static int delete_event_number;

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
        holder.textView1.setText(mItems.get(position).event_category);
        holder.textView2.setText(mItems.get(position).event_name);
        holder.textView3.setText(mItems.get(position).event_price);
        holder.textView4.setText(mItems.get(position).event_dis_price);
        holder.textView5.setText(mItems.get(position).event_startday);
        holder.textView6.setText(mItems.get(position).event_endday);

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

                toast = Toast.makeText(context , "취소 완료", Toast.LENGTH_SHORT);
                toast.show();

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

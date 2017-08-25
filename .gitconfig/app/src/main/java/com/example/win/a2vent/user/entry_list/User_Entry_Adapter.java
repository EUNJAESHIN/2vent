package com.example.win.a2vent.user.entry_list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.win.a2vent.R;
import com.example.win.a2vent.user.details_info.Activity_User_Event_Details_Info;
import com.example.win.a2vent.util.GlobalData;

import java.util.ArrayList;

/**
 * Created by EUNJAESHIN on 2017-08-03.
 */

public class User_Entry_Adapter extends RecyclerView.Adapter<User_Entry_Holder> {

    DeleteUserEntry deleteUserEntry;
    private Context mContext;
    private ArrayList<User_Entry_Item> mItems = new ArrayList<User_Entry_Item>();
    static int delete_event_number;

    public User_Entry_Adapter(ArrayList items, Context context) {
        mItems = items;
        mContext = context;
    }

    public static int getDelete_event_number() {
        return delete_event_number;
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

//        아이템 클릭리스너
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_goDetail = new Intent(mContext, Activity_User_Event_Details_Info.class);
                intent_goDetail.putExtra("event_number", mItems.get(position).getEventNum());
                // Intent에 event_number값을 담기
                mContext.startActivity(intent_goDetail);
            }
        });
//        롱클릭하면 삭제
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setGravity(Gravity.RIGHT);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(" 응모한 이벤트를 취소합니다.\n\n 취소할까요?")
                                .setCancelable(false)
                                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        delete_event_number = mItems.get(position).getEventNum();
                                        deleteUserEntry = new DeleteUserEntry();
                                        deleteUserEntry.execute
                                                (Integer.toString(mItems.get(position).getEventNum()),
                                                        GlobalData.getUserID());
                                        // 새로고침
                                        Intent intent_Refresh =
                                                new Intent(mContext, Activity_User_Entry_List.class);
                                        intent_Refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mContext.startActivity(intent_Refresh);
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
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

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
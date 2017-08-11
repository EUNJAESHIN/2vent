package com.example.win.a2vent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by win on 2017-08-08.
 */

public class Owner_Add_Event_Adapter extends RecyclerView.Adapter<Owner_Add_Event_Holder> {

    private final String TAG = "테스트";

    private ArrayList<Owner_Add_Event_Item> mArrayList = new ArrayList<>();
    private Context mContext;

    public Owner_Add_Event_Adapter(ArrayList<Owner_Add_Event_Item> arrayList, Context context) {
        mArrayList = arrayList;
        mContext = context;
    }

    @Override
    public Owner_Add_Event_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_add_event_item, parent, false);
        Owner_Add_Event_Holder holder = new Owner_Add_Event_Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Owner_Add_Event_Holder holder, final int position) {

        if (mArrayList.get(position).getBitmap() == null) {
            holder.getImageView().setImageResource(R.drawable.add);
        } else {
            holder.getImageView().setImageBitmap(mArrayList.get(position).getBitmap());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlobalData.ADD_EVENT_RECEIVER);
                intent.putExtra("add_image", "add");
                intent.putExtra("position", position);
                mContext.sendBroadcast(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final String[] item = new String[]{"삭제"};

                new AlertDialog.Builder(mContext).setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                Intent intent = new Intent(GlobalData.ADD_EVENT_RECEIVER);
                                intent.putExtra("add_image", "remove");
                                intent.putExtra("position", position);
                                mContext.sendBroadcast(intent);
                            }
                            break;
                        }
                    }
                }).show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}

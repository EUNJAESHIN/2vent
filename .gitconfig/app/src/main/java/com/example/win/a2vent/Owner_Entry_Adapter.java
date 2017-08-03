package com.example.win.a2vent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by win on 2017-08-01.
 */

public class Owner_Entry_Adapter extends RecyclerView.Adapter<Owner_Entry_Holder> {

    private final String TAG = "테스트";

    private ArrayList<Owner_Entry_Item> mArrayList;
    private Context mContext;

    public Owner_Entry_Adapter(ArrayList<Owner_Entry_Item> arrayList, Context context) {
        mArrayList = new ArrayList<>();
        mArrayList = arrayList;
        mContext = context;
    }

    @Override
    public Owner_Entry_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_entry_list_item, parent, false);
        Owner_Entry_Holder holder = new Owner_Entry_Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Owner_Entry_Holder holder, int position) {
        holder.getTvNo().setText(mArrayList.get(position).getNo());
        holder.getTvID().setText(mArrayList.get(position).getID());
        holder.getTvName().setText(mArrayList.get(position).getName());
        holder.getTvSex().setText(mArrayList.get(position).getSex());
        holder.getTvPhoneNo().setText(mArrayList.get(position).getPhoneNo());
        holder.getTvAddress().setText(mArrayList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}

package com.example.win.a2vent;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by EUNJAESHIN on 2017-08-22.
 */

public class Owner_Review_Adapter extends RecyclerView.Adapter<Owner_Review_Holder> {

    private Context context;
    private ArrayList<Owner_Review_Item> mItems = new ArrayList<Owner_Review_Item>();

    public Owner_Review_Adapter(ArrayList items, Context mContext) {
        mItems = items;
        context = mContext;
    }

    @Override
    public Owner_Review_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.owner_review_cardview, parent, false);
        Owner_Review_Holder holder = new Owner_Review_Holder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(Owner_Review_Holder holder, int position) {
        holder.imageView.setImageBitmap(mItems.get(position).event_IMG);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

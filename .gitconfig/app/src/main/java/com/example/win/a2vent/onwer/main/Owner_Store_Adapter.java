package com.example.win.a2vent.onwer.main;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.win.a2vent.R;
import com.example.win.a2vent.onwer.add_store.Activity_Owner_Store_Event_List;
import com.example.win.a2vent.util.DeleteData;
import com.example.win.a2vent.util.GlobalData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by win on 2017-07-03.
 */

public class Owner_Store_Adapter extends RecyclerView.Adapter<Owner_Store_Holder> {
    private ArrayList<Owner_Store_Item> mItems = new ArrayList<Owner_Store_Item>();
    private Context mContext;
    private int lastPosition = -1;

    public Owner_Store_Adapter(ArrayList<Owner_Store_Item> items, Context context) {
        mItems = items;
        mContext = context;
    }

    @Override
    public Owner_Store_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_store_cardview, parent, false);
        Owner_Store_Holder holder = new Owner_Store_Holder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(Owner_Store_Holder holder, final int position) {
        Picasso.with(mContext).load(GlobalData.getURL() + mItems.get(position).getCom_URI())
                .placeholder(R.drawable.store_default)
                .into(holder.getCom_imageView());
        holder.getCom_addr().setText(mItems.get(position).getCom_addr().concat("   ")
                .concat(mItems.get(position).getCom_detail_addr()));
        holder.getCom_name().setText(mItems.get(position).getCom_name());
        holder.getCom_manager().setText(mItems.get(position).getCom_manager());
        holder.getCom_category().setText(mItems.get(position).getCom_category());
        holder.getCom_number().setText(mItems.get(position).getCom_number());

        setAnimation(holder.getCom_imageView(), position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Activity_Owner_Store_Event_List.class);
                intent.putExtra("com_number", mItems.get(position).getCom_number());
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
                                        DeleteData deleteData = new DeleteData(mContext, "StoreKey", "com_number", mItems.get(position).getCom_number());
                                        deleteData.execute("2ventDeleteStore.php");
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

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

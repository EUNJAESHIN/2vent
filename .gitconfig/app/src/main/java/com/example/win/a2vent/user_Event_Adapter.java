package com.example.win.a2vent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 사용자 이벤트 받아오는 어댑터
 * <p>
 * 이미지 로드 라이브러리 Picasso
 * http://dwfox.tistory.com/33
 * AQuery (테스트만)
 * http://egloos.zum.com/mightyfine/v/307168
 */

public class user_Event_Adapter extends RecyclerView.Adapter<user_Event_Holder> {

    private Context context;
    private ArrayList<user_Event_Item> mItems = new ArrayList<user_Event_Item>();
    private int lastPosition = -1;

    public user_Event_Adapter(ArrayList items, Context mContext) {
        mItems = items;
        context = mContext;
    }

    @Override
    public user_Event_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_event_cardview, parent, false);
        user_Event_Holder holder = new user_Event_Holder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(user_Event_Holder holder, final int position) {
//        피카소 라이브러리 적용
        Picasso.with(context).load(GlobalData.getURL() + mItems.get(position).event_URI).
                placeholder(R.drawable.event_default).into(holder.imageView);

        holder.textView1.setText(mItems.get(position).event_name);
        holder.textView2.setText(mItems.get(position).event_price);
        holder.textView3.setText(mItems.get(position).event_dis_price);
        holder.textView4.setText(mItems.get(position).event_startday);
        holder.textView5.setText(mItems.get(position).event_endday);

//        아이템 클릭리스너 (RecyclerItemClickListener)
        holder.view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_goDetail = new Intent(context, user_Event_Detail.class);
                intent_goDetail.putExtra("event_number", mItems.get(position).event_number);
                // Intent에 event_number값을 담rl
                context.startActivity(intent_goDetail);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "롱 클릭", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        setAnimation(holder.imageView, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    //    이미지 애니메이션 기본
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}



package com.example.win.a2vent.user.main;

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

import com.example.win.a2vent.R;
import com.example.win.a2vent.user.details_info.Activity_User_Event_Details_Info;
import com.example.win.a2vent.util.GlobalData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.win.a2vent.user.account.Activity_User_Login.toast;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 사용자 이벤트 받아오는 어댑터
 * <p>
 * 이미지 로드 라이브러리 Picasso
 * http://dwfox.tistory.com/33
 * AQuery (테스트만)
 * http://egloos.zum.com/mightyfine/v/307168
 */

public class User_Event_Adapter extends RecyclerView.Adapter<User_Event_Holder> {

    private Context context;
    private ArrayList<User_Event_Item> mItems = new ArrayList<User_Event_Item>();
    private int lastPosition = -1;

    public User_Event_Adapter(ArrayList items, Context mContext) {
        mItems = items;
        context = mContext;
    }

    @Override
    public User_Event_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_event_cardview, parent, false);
        User_Event_Holder holder = new User_Event_Holder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(User_Event_Holder holder, final int position) {
//        피카소 라이브러리 적용
        Picasso.with(context).load(GlobalData.getURL() + mItems.get(position).getEventURI()).
                placeholder(R.drawable.event_default).into(holder.imageView);

        holder.textView1.setText(mItems.get(position).getEventName());
        holder.textView2.setText("정상가 : " + mItems.get(position).getEventPrice());
        holder.textView3.setText("할인가 : " + mItems.get(position).getEventDisprice());
        holder.textView4.setText(mItems.get(position).getEventStartday() +
                "  ~  " + mItems.get(position).getEventEndday());

//        아이템 클릭리스너
        holder.view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_goDetail = new Intent(context, Activity_User_Event_Details_Info.class);
                intent_goDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_goDetail.putExtra("event_number", mItems.get(position).getEventNum());
                // Intent에 event_number값을 담기
                context.startActivity(intent_goDetail);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toast = Toast.makeText(context, "롱 클릭", Toast.LENGTH_SHORT);
                toast.show();
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
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}



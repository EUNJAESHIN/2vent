package com.example.win.a2vent.onwer.add_event;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.R;
import com.example.win.a2vent.databinding.ActivityOwnerAddEventReviewBinding;

import java.util.ArrayList;

import static com.example.win.a2vent.user.account.Activity_User_Login.actList;

/**
 * Created by EUNJAESHIN on 2017-08-22.
 * 이벤트 미리보기
 */

public class Activity_Owner_Add_Event_Review extends AppCompatActivity {

    ActivityOwnerAddEventReviewBinding binding_Review;
    RecyclerView.Adapter rAdatper_reviewimg;
    ArrayList<Owner_Review_Item> image_All = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        binding_Review = DataBindingUtil.setContentView(this, R.layout.activity_owner_add_event_review);

        Intent intent_getReviewinfo = getIntent();

        for (int i = 0; intent_getReviewinfo.getExtras().getString("event_img_dir" +
                Integer.toString(i)) != null; i++) {
            String img_dir = intent_getReviewinfo.getExtras().getString("event_img_dir" +
                    Integer.toString(i));
            image_All.add(new Owner_Review_Item(img_dir));
        }

        binding_Review.tvOwnerReview1.append(intent_getReviewinfo.getExtras().getString("event_name"));
        binding_Review.tvOwnerReview2.append(intent_getReviewinfo.getExtras().getString("event_price"));
        binding_Review.tvOwnerReview2.setPaintFlags
                (binding_Review.tvOwnerReview2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding_Review.tvOwnerReview3.append(intent_getReviewinfo.getExtras().getString("event_dis_price"));
        binding_Review.tvOwnerReview4.append(intent_getReviewinfo.getExtras().getString("event_date"));
        binding_Review.tvOwnerReview5.append(intent_getReviewinfo.getExtras().getString("event_content"));
        binding_Review.tvOwnerReview9.append(intent_getReviewinfo.getExtras().getString("event_people"));


        rAdatper_reviewimg = new Owner_Review_Adapter(image_All, Activity_Owner_Add_Event_Review.this);
        binding_Review.rviewOwnerReview.setAdapter(rAdatper_reviewimg);
        rAdatper_reviewimg.notifyDataSetChanged();
    }

    public void onClick_review_participation(View v) {
        Toast.makeText(this, "이벤트에 참여하는 버튼입니다", Toast.LENGTH_SHORT).show();
    }


}

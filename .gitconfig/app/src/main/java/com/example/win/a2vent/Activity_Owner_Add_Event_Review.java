package com.example.win.a2vent;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.ActivityOwnerAddEventReviewBinding;

import static com.example.win.a2vent.Activity_User_Login.actList;

/**
 * Created by EUNJAESHIN on 2017-08-22.
 * 이벤트 미리보기
 */

public class Activity_Owner_Add_Event_Review extends AppCompatActivity {

    ActivityOwnerAddEventReviewBinding binding_Review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        binding_Review = DataBindingUtil.setContentView(this, R.layout.activity_owner_add_event_review);

        Intent intent_getReviewinfo = getIntent();
        binding_Review.tvOwnerReview1.append(intent_getReviewinfo.getExtras().getString("event_name"));
        binding_Review.tvOwnerReview2.append(intent_getReviewinfo.getExtras().getString("event_price"));
        binding_Review.tvOwnerReview3.append(intent_getReviewinfo.getExtras().getString("event_dis_price"));
        binding_Review.tvOwnerReview4.append(intent_getReviewinfo.getExtras().getString("event_date"));
        binding_Review.tvOwnerReview5.append(intent_getReviewinfo.getExtras().getString("event_content"));

    }

    public void onClick_review_participation(View v) {
        Toast.makeText(this, "남은 자리를 확인하고 이벤트에 참여하는 버튼입니다", Toast.LENGTH_SHORT).show();
    }
}

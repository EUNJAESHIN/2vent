package com.example.win.a2vent;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.win.a2vent.databinding.ActivityUserEventOkBinding;

import static com.example.win.a2vent.Activity_User_Login.actList;

public class Activity_User_Event_OK extends Activity {

    static ActivityUserEventOkBinding binding_UserEventOK;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding_UserEventOK = DataBindingUtil.setContentView(this, R.layout.activity_user_event_ok);
        this.setFinishOnTouchOutside(false); // 다이얼로그 액티비티 외부 터치시 finish false

        Intent intent_getIsEntry = getIntent();
        result = intent_getIsEntry.getExtras().getString("result");
        // 이벤트 참여시 가져온 result값을 받아온다

        if (result.equals("성공")) {
            binding_UserEventOK.eventOkImageView.setImageResource(R.drawable.user_info);
            binding_UserEventOK.eventOkTextView.append("이벤트 참여 성공");
        } else if (result.equals("남은 자리 없음")) {
            binding_UserEventOK.eventOkImageView.setImageResource(R.drawable.user_info);
            binding_UserEventOK.eventOkTextView.append("이미 모집이 끝났습니다..");
        } else if (result.equals("중복 에러")) {
            binding_UserEventOK.eventOkImageView.setImageResource(R.drawable.user_info);
            binding_UserEventOK.eventOkTextView.append("이미 참여한 이벤트입니다");
        } else {
            binding_UserEventOK.eventOkImageView.setImageResource(R.drawable.user_info);
            binding_UserEventOK.eventOkTextView.append("Entry Error");
        } // result값에 따라 정보 표시
    }

    public void onClick_Confirm(View v) { this.finish(); } // 확인 후 닫기 버튼

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    } // 백키 비활성화

}

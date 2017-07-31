package com.example.win.a2vent;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.UserInfoBinding;

/**
 * Created by EUNJAESHIN on 2017-07-26.
 * 사용자 상세정보 부분
 */

public class user_Info extends AppCompatActivity {
    UserInfoBinding binding_UserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_UserInfo = DataBindingUtil.setContentView(this, R.layout.user_info);

        binding_UserInfo.tvInfo1.append("아이디  : " + GlobalData.getUserID());
        binding_UserInfo.tvInfo2.append("비밀번호 : " + GlobalData.getUserPW());
        binding_UserInfo.tvInfo3.append("성명 : " + GlobalData.getUserName());
        binding_UserInfo.tvInfo4.append("주소 : " + GlobalData.getUserAddr());
        binding_UserInfo.tvInfo5.append("생년월일 : " + GlobalData.getUserBirth());
        if (GlobalData.getUserSex().equals("0")) {
            binding_UserInfo.tvInfo6.append("성별 : 여성");
        } else if (GlobalData.getUserSex().equals("1")) {
            binding_UserInfo.tvInfo6.append("성별 : 남성");
        } else {
            binding_UserInfo.tvInfo6.append("성별 : ");
        }
        binding_UserInfo.tvInfo7.append("전화번호 : " + GlobalData.getUserPhone());
        binding_UserInfo.tvInfo8.append("계좌번호 : " + GlobalData.getUserAccountNum());

    }

    public void onClick_withdrawal(View v) {
        Toast.makeText(this, "누르면 회원 탈퇴", Toast.LENGTH_SHORT).show();
        //TODO : 회원 탈퇴
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

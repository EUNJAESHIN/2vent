package com.example.win.a2vent;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.win.a2vent.databinding.ActivityUserInfoModifyBinding;

public class Activity_User_Info_Modify extends Activity {
    ActivityUserInfoModifyBinding binding_UserInfoModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding_UserInfoModify = DataBindingUtil.setContentView(this, R.layout.activity_user_info_modify);


    }


}

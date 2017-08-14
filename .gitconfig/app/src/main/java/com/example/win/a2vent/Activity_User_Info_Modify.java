package com.example.win.a2vent;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.win.a2vent.databinding.ActivityUserInfoModifyBinding;

import static com.example.win.a2vent.Activity_User_Login.actList;
import static com.example.win.a2vent.Activity_User_Login.toast;

public class Activity_User_Info_Modify extends Activity {

    ActivityUserInfoModifyBinding binding_UserInfoModify;
    String pw1, pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding_UserInfoModify = DataBindingUtil.setContentView(this, R.layout.activity_user_info_modify);

    }

    public void onClick_ModifyDone(View v) {
        pw1 = GlobalData.setSHA256(binding_UserInfoModify.eTextModifyPw1.getText().toString());

        if (pw1.equals(GlobalData.getUserPW())) {
            toast = Toast.makeText(Activity_User_Info_Modify.this, "ㅇㅋ", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onClick_ModifyCancle(View v) {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }


}

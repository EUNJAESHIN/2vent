package com.example.win.a2vent;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Vibrator;
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
    Vibrator vibe;
    String pw1, pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        binding_UserInfoModify = DataBindingUtil.setContentView(this, R.layout.activity_user_info_modify);

    }

    public void onClick_ModifyDone(View v) {
        binding_UserInfoModify.eTextModifyPw1.setHint("현재 비밀번호");
        binding_UserInfoModify.eTextModifyPw2.setHint("변경할 비밀번호");
        pw1 = GlobalData.setSHA256(binding_UserInfoModify.eTextModifyPw1.getText().toString());
        pw2 = GlobalData.setSHA256(binding_UserInfoModify.eTextModifyPw2.getText().toString());

        if (pw1.equals(GlobalData.getUserPW()) && !pw2.equals(GlobalData.getUserPW())
                && !binding_UserInfoModify.eTextModifyPw2.getText().toString().equals("")) {
            toast = Toast.makeText(Activity_User_Info_Modify.this, "ㅇㅋ", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!pw1.equals(GlobalData.getUserPW())) {
            binding_UserInfoModify.eTextModifyPw1.setText("");
            binding_UserInfoModify.eTextModifyPw1.setHint("현재 비밀번호가 일치하지 않음");
            vibe.vibrate(150);
        } else if (pw1.equals(GlobalData.getUserPW()) && pw2.equals(GlobalData.getUserPW())) {
            binding_UserInfoModify.eTextModifyPw2.setText("");
            binding_UserInfoModify.eTextModifyPw2.setHint("바꿀 비밀번호가 현재 비밀번호와 같음");
            vibe.vibrate(150);
        } else if (pw1.equals(GlobalData.getUserPW()) && !pw2.equals(GlobalData.getUserPW())
                && binding_UserInfoModify.eTextModifyPw2.getText().toString().equals("")) {
            binding_UserInfoModify.eTextModifyPw2.setText("");
            binding_UserInfoModify.eTextModifyPw2.setHint("변경할 비밀번호 미입력");
            vibe.vibrate(150);
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

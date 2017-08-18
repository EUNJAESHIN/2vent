package com.example.win.a2vent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
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

/**
 * Created by EUNJAESHIN on 2017-08-16
 */

public class Activity_User_Info_Modify extends Activity {

    ActivityUserInfoModifyBinding binding_UserInfoModify;
    Vibrator vibe;
    String pw1, pw2;
    ModifyPW modifyPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        binding_UserInfoModify = DataBindingUtil.setContentView(this, R.layout.activity_user_info_modify);
        this.setFinishOnTouchOutside(false); // 다이얼로그 액티비티 외부 터치시 finish false

    }

    public void onClick_ModifyDone(View v) {
        binding_UserInfoModify.eTextModifyPw1.setHint("현재 비밀번호");
        binding_UserInfoModify.eTextModifyPw2.setHint("변경할 비밀번호");
        pw1 = GlobalData.setSHA256(binding_UserInfoModify.eTextModifyPw1.getText().toString());
        pw2 = GlobalData.setSHA256(binding_UserInfoModify.eTextModifyPw2.getText().toString());

        if (pw1.equals(GlobalData.getUserPW()) && !pw2.equals(GlobalData.getUserPW())
                && !binding_UserInfoModify.eTextModifyPw2.getText().toString().equals("")) {
            modifyPW = new ModifyPW();
            modifyPW.execute(GlobalData.getUserID(), pw1, pw2);
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
    } // 비밀번호 변경

    public void onClick_ModifyCancel(View v) {
        this.finish();
    } // 비밀번호 변경 취소

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    } // 백키 비활성화

    private class ModifyPW extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "2ventModifyPassword.php";

            try {
                ServerConnector serverConnector = new ServerConnector(serverURL);

                serverConnector.addPostData("id", params[0]);
                serverConnector.addPostData("pw1", params[1]);
                serverConnector.addPostData("pw2", params[2]);

                serverConnector.addDelimiter();
                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d("DB", "ModifyPW Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("변경 완료")) {
                Intent intent_Modifydone = new Intent(Activity_User_Info_Modify.this, Activity_User_Login.class);
                intent_Modifydone.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_Modifydone);
                toast = Toast.makeText(Activity_User_Info_Modify.this, "변경 완료", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                toast = Toast.makeText(Activity_User_Info_Modify.this, "Modify Error", Toast.LENGTH_SHORT);
                toast.show();
            }
            Log.d("DB", "POST response - " + result);
        }

    } // 비밀번호 변경 AsyncTask

    @Override
    protected void onDestroy() {
        if (modifyPW != null) {
            modifyPW.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (modifyPW != null) {
            modifyPW.cancel(true);
        }
        super.onPause();
    }

}

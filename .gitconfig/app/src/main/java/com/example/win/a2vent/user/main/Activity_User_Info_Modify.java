package com.example.win.a2vent.user.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.win.a2vent.user.account.Activity_User_Login;
import com.example.win.a2vent.util.GlobalData;
import com.example.win.a2vent.R;
import com.example.win.a2vent.util.ServerConnector;
import com.example.win.a2vent.databinding.ActivityUserInfoModifyBinding;

/**
 * Created by EUNJAESHIN on 2017-08-16
 */

public class Activity_User_Info_Modify extends Activity {

    ActivityUserInfoModifyBinding binding_UserInfoModify;
    String pw1, pw2;
    ModifyPW modifyPW;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity_User_Login.actList.add(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding_UserInfoModify = DataBindingUtil.setContentView(this, R.layout.activity_user_info_modify);
        this.setFinishOnTouchOutside(false); // 다이얼로그 액티비티 외부 터치시 finish false

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        binding_UserInfoModify.layoutModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.hideKeyboard(imm, v);
            }
        }); // 빈화면 터치시 키보드 내림
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
        } else if (!pw1.equals(GlobalData.getUserPW()) &&
                !binding_UserInfoModify.eTextModifyPw1.getText().toString().equals("")) {
            binding_UserInfoModify.eTextModifyPw1.setText("");
            binding_UserInfoModify.eTextModifyPw1.setHint("현재 비밀번호가 일치하지 않음");
            binding_UserInfoModify.eTextModifyPw1.requestFocus();
            GlobalData.showKeyboard(imm);
        } else if (pw1.equals(GlobalData.getUserPW()) && pw2.equals(GlobalData.getUserPW())) {
            binding_UserInfoModify.eTextModifyPw2.setText("");
            binding_UserInfoModify.eTextModifyPw2.setHint("바꿀 비밀번호가 현재 비밀번호와 같음");
            binding_UserInfoModify.eTextModifyPw2.requestFocus();
            GlobalData.showKeyboard(imm);
        } else if (!pw1.equals(GlobalData.getUserPW()) &&
                binding_UserInfoModify.eTextModifyPw1.getText().toString().equals("")) {
            binding_UserInfoModify.eTextModifyPw1.setText("");
            binding_UserInfoModify.eTextModifyPw1.setHint("현재 비밀번호 미입력");
            binding_UserInfoModify.eTextModifyPw1.requestFocus();
            GlobalData.showKeyboard(imm);
        } else if (pw1.equals(GlobalData.getUserPW()) && !pw2.equals(GlobalData.getUserPW())
                && binding_UserInfoModify.eTextModifyPw2.getText().toString().equals("")) {
            binding_UserInfoModify.eTextModifyPw2.setText("");
            binding_UserInfoModify.eTextModifyPw2.setHint("변경할 비밀번호 미입력");
            binding_UserInfoModify.eTextModifyPw2.requestFocus();
            GlobalData.showKeyboard(imm);
        }
    } // 비밀번호 수정 버튼

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
                Activity_User_Login.toast = Toast.makeText(Activity_User_Info_Modify.this, "변경 완료", Toast.LENGTH_SHORT);
                Activity_User_Login.toast.show();
            } else {
                Activity_User_Login.toast = Toast.makeText(Activity_User_Info_Modify.this, "Modify Error", Toast.LENGTH_SHORT);
                Activity_User_Login.toast.show();
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

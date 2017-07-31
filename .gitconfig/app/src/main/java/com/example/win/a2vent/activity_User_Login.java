package com.example.win.a2vent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.ActivityUserLoginBinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 로그인 부분 (첫 액티비티)
 */

public class activity_User_Login extends AppCompatActivity {

    private long backKeyPressedTime = 0;
    String sId, sPw;
    ActivityUserLoginBinding binding_userLogin;
    LoginDB loginDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_userLogin = DataBindingUtil.setContentView(this, R.layout.activity_user_login);

    }

    public void onClick_login(View view) {
        try {
            sId = binding_userLogin.eTextLoginId.getText().toString();
            sPw = binding_userLogin.eTextLoginPw.getText().toString();
        } catch (NullPointerException e) {
            Log.e("onClick_login Error :", e.getMessage());
        }

        loginDB = new LoginDB();
        loginDB.execute(sId, sPw);
    }

    public void onClick_join(View view) {
        Intent intent_join = new Intent(activity_User_Login.this, activity_User_Join.class);
        startActivity(intent_join);
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(activity_User_Login.this,
                    "'뒤로' 버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            Toast.makeText(activity_User_Login.this,
                    "'뒤로' 버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).cancel();
            finish();
        }
    } // 백키 2번해야 종료

    class LoginDB extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(activity_User_Login.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "2ventLogin.php";

            try {
                ServerConnector serverConnector = new ServerConnector(serverURL);

                serverConnector.addPostData("id", params[0]);
                serverConnector.addPostData("pw", params[1]);

                serverConnector.addDelimiter();
                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d("DB", "LoginDB Error : ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

//            2ventLogin.php의 결과값과 비교하여 로그인 성공 및 실패
            if (result.equals("0")) {
                Toast.makeText(activity_User_Login.this, "Account Error", Toast.LENGTH_SHORT).show();
            } else if (result.equals("1")) {
                GlobalData.setLogin_ID(binding_userLogin.eTextLoginId.getText().toString());
                Intent intent_userLogin = new Intent(activity_User_Login.this, user_Event_Main.class);
                startActivity(intent_userLogin);
            } else if (result.equals("2")) {
                GlobalData.setLogin_ID(binding_userLogin.eTextLoginId.getText().toString());
                Intent intent_managerLogin = new Intent(activity_User_Login.this, owner_Event_Main.class);
                startActivity(intent_managerLogin);
            } else {
                Toast.makeText(activity_User_Login.this, "Account Error", Toast.LENGTH_SHORT).show();
            }

            Log.d("DB", "POST response - " + result);
        }

    } // 회원 DB값 비교 AsyncTask


    @Override
    protected void onDestroy() {
        if (loginDB != null) {
            loginDB.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (loginDB != null) {
            loginDB.cancel(true);
        }
        super.onPause();
    }
}

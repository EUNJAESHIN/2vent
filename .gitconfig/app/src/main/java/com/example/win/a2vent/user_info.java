package com.example.win.a2vent;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.UserInfoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class user_Info extends AppCompatActivity {
    private String TAG = "getUserInfo";
    UserInfoBinding binding_UserInfo;
    GetUserInfo getUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreates(savedInstanceState);
        binding_UserInfo = DataBindingUtil.setContentView(this, R.layout.user_info);

        getUserInfo = new GetUserInfo();
        getUserInfo.execute(GlobalData.getLogin_ID());
    }

    public void onClick_withdrawal(View v) {
        Toast.makeText(this, "누르면 회원 탈퇴", Toast.LENGTH_SHORT).show();
        //TODO : 회원 탈퇴
    }

    private class GetUserInfo extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "2ventGetUserInfo.php";

            try {
                ServerConnector serverConnector = new ServerConnector(serverURL);

                serverConnector.addPostData("id", params[0]);

                serverConnector.addDelimiter();
                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d("DB", "GetUserInfo Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response  - " + result);

            if (result == null) {

            } else {
                setUserInfo(result);
            }
        }

    } // UserDB 받는 AsyncTask

    private void setUserInfo(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("User");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString("id");
                String pw = item.getString("pw");
                String name = item.getString("name");
                String addr = item.getString("addr");
                String birthday = item.getString("birthday");
                String sex = item.getString("sex");
                String phone = item.getString("phone");
                String account_number = item.getString("account_number");

                binding_UserInfo.tvInfo1.append("아이디  : " + id);
                binding_UserInfo.tvInfo2.append("비밀번호 : " + pw);
                binding_UserInfo.tvInfo3.append("성명 : " + name);
                binding_UserInfo.tvInfo4.append("주소 : " + addr);
                binding_UserInfo.tvInfo5.append("생년월일 : " + birthday);
                if (sex.equals("0")) {
                    binding_UserInfo.tvInfo6.append("성별 : 여성");
                } else if (sex.equals("1")) {
                    binding_UserInfo.tvInfo6.append("성별 : 남성");
                } else {
                    binding_UserInfo.tvInfo6.append("성별 : ");
                }
                binding_UserInfo.tvInfo7.append("전화번호 : " + phone);
                binding_UserInfo.tvInfo8.append("계좌번호 : " + account_number);

            }

        } catch (JSONException e) {
            Log.d(TAG, "setUserInfo Error : ", e);
        }
    } // 유저정보 JSON 데이터를 텍스트뷰에 표시

    @Override
    protected void onDestroy() {
        if (getUserInfo != null) {
            getUserInfo.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (getUserInfo != null) {
            getUserInfo.cancel(true);
        }
        super.onPause();
    }
}

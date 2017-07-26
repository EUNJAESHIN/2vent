package com.example.win.a2vent;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.win.a2vent.databinding.UserInfoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class user_Info extends AppCompatActivity {
    private String TAG = "getUserInfo";
    String mJsonString;
    UserInfoBinding binding_UserInfo;
    getUserInfo getUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_UserInfo = DataBindingUtil.setContentView(this, R.layout.user_info);

        getUserInfo = new getUserInfo();
        getUserInfo.execute(GlobalData.getURL() + "2ventGetUserInfo.php");
    }

    private class getUserInfo extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(user_Info.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "getEventDB Error : ", e);
                errorString = e.toString();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null) {

            } else {
                mJsonString = result;
                setUserInfo();
            }
        }

    } // EventDB 받는 AsyncTask

    private void setUserInfo() {

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
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

                if (id.equals(GlobalData.getLogin_ID())) {
                    binding_UserInfo.tvInfo1.append("아이디  : " + id);
                    binding_UserInfo.tvInfo2.append("비밀번호 : " + pw);
                    binding_UserInfo.tvInfo3.append("성명 : " + name);
                    binding_UserInfo.tvInfo4.append("주소 : " + addr);
                    binding_UserInfo.tvInfo5.append("생년월일 : " + birthday);
                    if(sex.equals("0")) {
                        binding_UserInfo.tvInfo6.append("성별 : 여성");
                    } else if (sex.equals("1")) {
                        binding_UserInfo.tvInfo6.append("성별 : 남성");
                    } else {
                        binding_UserInfo.tvInfo6.append("성별 : ");
                    }
                    binding_UserInfo.tvInfo7.append("전화번호 : " + phone);
                    binding_UserInfo.tvInfo8.append("계좌번호 : " + account_number);
                }
            }

        } catch (JSONException e) {
            Log.d(TAG, "addItemInCategory Error : ", e);
        }
    } // JSON 데이터를 카테고리에 저장

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

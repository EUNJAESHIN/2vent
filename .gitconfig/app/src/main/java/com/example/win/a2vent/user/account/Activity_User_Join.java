package com.example.win.a2vent.user.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.win.a2vent.R;
import com.example.win.a2vent.util.ServerConnector;
import com.example.win.a2vent.databinding.ActivityUserJoinBinding;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 회원가입 부분
 */

public class Activity_User_Join extends AppCompatActivity {

    ActivityUserJoinBinding binding_userJoin;
    String sex, user_type;
    String id, pw, name, addr, birthday, phone, account_number; // 회원가입 시 사용될 String 변수
    JoinDB joinDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity_User_Login.actList.add(this);
        binding_userJoin = DataBindingUtil.setContentView(this, R.layout.activity_user_join);

        // 정규표현식으로 문자열 입력 제한
        binding_userJoin.eTextJoinId.setFilters(new InputFilter[]{InputFilters.filter});
        binding_userJoin.eTextJoinPw.setFilters(new InputFilter[]{InputFilters.filterPw});
        binding_userJoin.eTextJoinName.setFilters(new InputFilter[]{InputFilters.filterKor});
        binding_userJoin.eTextJoinBirth.setFilters(new InputFilter[]{InputFilters.filterNum});
        binding_userJoin.eTextJoinPhone.setFilters(new InputFilter[]{InputFilters.filterNum});
        binding_userJoin.eTextJoinAccountnumber.setFilters(new InputFilter[]{InputFilters.filterNum});

        // 라디오버튼 체크리스너
        binding_userJoin.rGroupSex.setOnCheckedChangeListener
                (new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        switch (checkedId) {
                            case R.id.rBt_sex0:
                                sex = "0";
                                break;
                            case R.id.rBt_sex1:
                                sex = "1";
                                break;
                        }
                    }
                });
        binding_userJoin.rGroupType.setOnCheckedChangeListener
                (new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        switch (checkedId) {
                            case R.id.rBt_user0:
                                user_type = "1";
                                break;
                            case R.id.rBt_user1:
                                user_type = "2";
                                break;
                            case R.id.rBt_user2:
                                user_type = "2";
                                break;
                        }
                    }
                });

    }

    public void onClick_joinOK(View view) {
        //TODO 여백 입력 제한
        try {
            id = binding_userJoin.eTextJoinId.getText().toString();
            pw = binding_userJoin.eTextJoinPw.getText().toString();
            name = binding_userJoin.eTextJoinName.getText().toString();
            addr = binding_userJoin.eTextJoinAddr.getText().toString();
            birthday = binding_userJoin.eTextJoinBirth.getText().toString();
            phone = binding_userJoin.eTextJoinPhone.getText().toString();
            account_number = binding_userJoin.eTextJoinAccountnumber.getText().toString();
        } catch (NullPointerException e) {
            Log.e("Join Error : ", e.getMessage());
        }

        joinDB = new JoinDB();
        joinDB.execute(id, pw, name, addr, birthday, sex, phone, user_type, account_number);
    } // 회원가입 버튼

    private class JoinDB extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Activity_User_Join.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "2ventRegister.php";

            try {
                ServerConnector serverConnector = new ServerConnector(serverURL);

                serverConnector.addPostData("id", params[0]);
                serverConnector.addPostData("pw", params[1]);
                serverConnector.addPostData("name", params[2]);
                serverConnector.addPostData("addr", params[3]);
                serverConnector.addPostData("birthday", params[4]);
                serverConnector.addPostData("sex", params[5]);
                serverConnector.addPostData("phone", params[6]);
                serverConnector.addPostData("user_type", params[7]);
                serverConnector.addPostData("account_number", params[8]);

                serverConnector.addDelimiter();
                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d("DB", "JoinDB Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

//            2ventRegister.php의 echo(result)와 비교하여 회원가입 성공 및 실패
            if (result.equals("회원가입 성공!")) {
                Intent intent_Joindone = new Intent(Activity_User_Join.this, Activity_User_Login.class);
                intent_Joindone.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_Joindone);
                Activity_User_Login.toast = Toast.makeText(Activity_User_Join.this, "가입 성공!", Toast.LENGTH_SHORT);
                Activity_User_Login.toast.show();
            } else if (result.equals("아이디가 존재합니다")) {
                Activity_User_Login.toast = Toast.makeText(Activity_User_Join.this, "아이디가 존재합니다", Toast.LENGTH_SHORT);
                Activity_User_Login.toast.show();
            } else {
                Activity_User_Login.toast = Toast.makeText(Activity_User_Join.this, "Register Error", Toast.LENGTH_SHORT);
                Activity_User_Login.toast.show();
            }
            Log.d("DB", "POST response  - " + result);
        }

    } // 회원 DB입력 AsynTask

    @Override
    protected void onDestroy() {
        if (joinDB != null) {
            joinDB.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (joinDB != null) {
            joinDB.cancel(true);
        }
        super.onPause();
    }

}


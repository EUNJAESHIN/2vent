package com.example.win.a2vent.user.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.win.a2vent.R;
import com.example.win.a2vent.onwer.add_store.Activity_Owner_Add_Store_WebView;
import com.example.win.a2vent.util.GlobalData;
import com.example.win.a2vent.util.ServerConnector;
import com.example.win.a2vent.databinding.ActivityUserJoinBinding;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 회원가입 부분
 */

public class Activity_User_Join extends AppCompatActivity {

    ActivityUserJoinBinding binding_userJoin;
    private static final int SEARCH_ADDRESS = 4;
    String sex, user_type;
    String id, pw, name, addr, birthday, phone, account_number; // 회원가입 시 사용될 String 변수
    JoinDB joinDB;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity_User_Login.actList.add(this);
        binding_userJoin = DataBindingUtil.setContentView(this, R.layout.activity_user_join);

        binding_userJoin.eTextJoinId.setFilters(new InputFilter[]{InputFilters.filter});
        binding_userJoin.eTextJoinPw.setFilters(new InputFilter[]{InputFilters.filterPw});
        binding_userJoin.eTextJoinName.setFilters(new InputFilter[]{InputFilters.filterKor});
        binding_userJoin.eTextJoinBirth.setFilters(new InputFilter[]{InputFilters.filterNum});
        binding_userJoin.eTextJoinPhone.setFilters(new InputFilter[]{InputFilters.filterNum});
        binding_userJoin.eTextJoinAccountnumber.setFilters(new InputFilter[]{InputFilters.filterNum});
        // 정규표현식으로 문자열 입력 제한

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        binding_userJoin.layoutJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.hideKeyboard(imm, v);
            }
        }); // 빈화면 터치시 키보드 내림

        binding_userJoin.eTextJoinAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.hideKeyboard(imm, v);
                Intent i = new Intent(Activity_User_Join.this, Activity_Owner_Add_Store_WebView.class);
                startActivityForResult(i, SEARCH_ADDRESS);
            }
        }); // 주소입력창 클릭시 주소검색 웹뷰로

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ADDRESS) {
            try {
                binding_userJoin.eTextJoinAddr.setText(data.getStringExtra("addr"));
                if (data.getStringExtra("addr") != "") {
                    binding_userJoin.eTextJoinBirth.requestFocus();
                    GlobalData.showKeyboard(imm);
                } // 주소입력되면 다음 항목에 포커스주고 키보드 올림
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    } // 검색한 주소값 받아옴

    public void onClick_joinOK(View view) {
        id = binding_userJoin.eTextJoinId.getText().toString();
        pw = binding_userJoin.eTextJoinPw.getText().toString();
        name = binding_userJoin.eTextJoinName.getText().toString();
        addr = binding_userJoin.eTextJoinAddr.getText().toString();
        birthday = binding_userJoin.eTextJoinBirth.getText().toString();
        phone = binding_userJoin.eTextJoinPhone.getText().toString();
        account_number = binding_userJoin.eTextJoinAccountnumber.getText().toString();

        if (id.equals("") || pw.equals("") || name.equals("") || addr.equals("") ||
                birthday.equals("") || sex == null || phone.equals("") || user_type == null ||
                account_number.equals("")) {
            checkBlank(); // 공백 체크
        } else {
            joinDB = new JoinDB();
            joinDB.execute(id, pw, name, addr, birthday, sex, phone, user_type, account_number);
        }
    } // 회원가입 버튼

    public void checkBlank() {
        if (id.equals("")) {
            binding_userJoin.eTextJoinId.setHint("아이디를 입력해주세요 (영문, 숫자)");
            binding_userJoin.eTextJoinId.requestFocus();
            GlobalData.showKeyboard(imm);
        } else if (pw.equals("")) {
            binding_userJoin.eTextJoinPw.setHint("비밀번호를 입력해주세요 (특수문자 허용)");
            binding_userJoin.eTextJoinPw.requestFocus();
            GlobalData.showKeyboard(imm);
        } else if (name.equals("")) {
            binding_userJoin.eTextJoinName.setHint("성함을 입력해주세요");
            binding_userJoin.eTextJoinName.requestFocus();
            GlobalData.showKeyboard(imm);
        } else if (addr.equals("")) {
            Toast.makeText(this, "주소를 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (birthday.equals("")) {
            binding_userJoin.eTextJoinBirth.setHint("생년월일을 입력해주세요 ex)19900101");
            binding_userJoin.eTextJoinBirth.requestFocus();
            GlobalData.showKeyboard(imm);
        } else if (sex == null) {
            Toast.makeText(this, "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
        } else if (phone.equals("")) {
            binding_userJoin.eTextJoinPhone.setHint("전화번호를 입력해주세요 ('-' 없이)");
            binding_userJoin.eTextJoinPhone.requestFocus();
            GlobalData.showKeyboard(imm);
        } else if (user_type == null) {
            Toast.makeText(this, "사용자 유형을 선택해주세요", Toast.LENGTH_SHORT).show();
        } else if (account_number.equals("")) {
            binding_userJoin.eTextJoinAccountnumber.setHint("계좌번호를 입력해주세요 ('-' 없이)");
            binding_userJoin.eTextJoinAccountnumber.requestFocus();
            GlobalData.showKeyboard(imm);
        }
    } // 공백 체크

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


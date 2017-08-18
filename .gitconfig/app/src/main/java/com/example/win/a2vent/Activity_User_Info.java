package com.example.win.a2vent;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.ActivityUserInfoBinding;

import static com.example.win.a2vent.Activity_User_Login.actList;
import static com.example.win.a2vent.Activity_User_Login.toast;

/**
 * Created by EUNJAESHIN on 2017-07-26.
 * 사용자 상세정보 부분
 */

public class Activity_User_Info extends AppCompatActivity {
    ActivityUserInfoBinding binding_UserInfo;
    AlertDialog.Builder builder_WithdrawalAlert;
    LeavingTask leavingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        binding_UserInfo = DataBindingUtil.setContentView(this, R.layout.activity_user_info);

        try {
            binding_UserInfo.tvInfo1.append(GlobalData.getUserID());
//            binding_UserInfo.tvInfo2.append("비밀번호 : " + GlobalData.getUserPW());
            binding_UserInfo.tvInfo3.append(GlobalData.getUserName());
            binding_UserInfo.tvInfo4.append(GlobalData.getUserAddr());
            binding_UserInfo.tvInfo5.append(GlobalData.getUserBirth());
            if (GlobalData.getUserSex().equals("0")) {
                binding_UserInfo.tvInfo6.append("여성");
            } else if (GlobalData.getUserSex().equals("1")) {
                binding_UserInfo.tvInfo6.append("남성");
            } else {
                binding_UserInfo.tvInfo6.append("");
            }
            binding_UserInfo.tvInfo7.append(GlobalData.getUserPhone());
            binding_UserInfo.tvInfo8.append(GlobalData.getUserAccountNum());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_ModifyInfo(View v) {
        Intent intent_ModifyInfo = new Intent(Activity_User_Info.this, Activity_User_Info_Modify.class);
        startActivity(intent_ModifyInfo);
    } // 비밀번호 변경 버튼

    public void onClick_Withdrawal(View v) {
        builder_WithdrawalAlert = new AlertDialog.Builder(Activity_User_Info.this);
        builder_WithdrawalAlert
                .setTitle("회원 탈퇴")
                .setMessage("\n 탈퇴 시, 참여한 모든 이벤트가 삭제됩니다. \n 탈퇴하시겠습니까?")
                .setCancelable(true)
                .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leavingTask = new LeavingTask();
                        leavingTask.execute(GlobalData.getUserID());
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog_WithdrawalAlert = builder_WithdrawalAlert.create();
        dialog_WithdrawalAlert.show();
    } // User 탈퇴 버튼

    private class LeavingTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Activity_User_Info.this, "Bye Bye..", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "2ventWithdrawal_user.php";

            try {
                ServerConnector serverConnector = new ServerConnector(serverURL);

                serverConnector.addPostData("id", params[0]);

                serverConnector.addDelimiter();
                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d("DB", "WithdrawalTask Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result.equals("탈퇴 처리 완료")) {
                Intent intent_Leavingdone = new Intent(Activity_User_Info.this, Activity_User_Login.class);
                for (int i = 0; i < actList.size(); i++)
                    actList.get(i).finish();
                startActivity(intent_Leavingdone);
                finish();
                toast = Toast.makeText(Activity_User_Info.this, "탈퇴 되었습니다", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                toast = Toast.makeText(Activity_User_Info.this, "Withdrawal Error", Toast.LENGTH_SHORT);
                toast.show();
            }
            Log.d("DB", "POST response  - " + result);
        }

    } // 회원 탈퇴 AsynTask

    @Override
    protected void onDestroy() {
        if (leavingTask != null) {
            leavingTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (leavingTask != null) {
            leavingTask.cancel(true);
        }
        super.onPause();
    }
}

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

import com.example.win.a2vent.databinding.OwnerInfoBinding;

/**
 * Created by EUNJAESHIN on 2017-07-26.
 * 사용자 상세정보 부분
 */

public class owner_Info extends AppCompatActivity {
    OwnerInfoBinding binding_OwnerInfo;
    AlertDialog.Builder builder_WithdrawalAlert;
    LeavingTask leavingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_OwnerInfo = DataBindingUtil.setContentView(this, R.layout.owner_info);

        builder_WithdrawalAlert = new AlertDialog.Builder(owner_Info.this);
        builder_WithdrawalAlert
                .setTitle("회원 탈퇴")
                .setMessage(" 탈퇴 시, 등록된 매장 모두가 삭제되고 " +
                        "\n 추가했던 모든 이벤트가 삭제됩니다. \n 탈퇴하시겠습니까?")
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
                        Toast.makeText(owner_Info.this, "탈퇴 취소", Toast.LENGTH_SHORT).show();
                    }
                });

        binding_OwnerInfo.tvOwnerinfo1.append("아이디  : " + GlobalData.getUserID());
        binding_OwnerInfo.tvOwnerinfo2.append("비밀번호 : " + GlobalData.getUserPW());
        binding_OwnerInfo.tvOwnerinfo3.append("성명 : " + GlobalData.getUserName());
        binding_OwnerInfo.tvOwnerinfo4.append("주소 : " + GlobalData.getUserAddr());
        binding_OwnerInfo.tvOwnerinfo5.append("생년월일 : " + GlobalData.getUserBirth());
        if (GlobalData.getUserSex().equals("0")) {
            binding_OwnerInfo.tvOwnerinfo6.append("성별 : 여성");
        } else if (GlobalData.getUserSex().equals("1")) {
            binding_OwnerInfo.tvOwnerinfo6.append("성별 : 남성");
        } else {
            binding_OwnerInfo.tvOwnerinfo6.append("성별 : ");
        }
        binding_OwnerInfo.tvOwnerinfo7.append("전화번호 : " + GlobalData.getUserPhone());
        binding_OwnerInfo.tvOwnerinfo8.append("계좌번호 : " + GlobalData.getUserAccountNum());

    }

    public void onClick_Withdrawal(View v) {
        AlertDialog dialog_WithdrawalAlert = builder_WithdrawalAlert.create();
        dialog_WithdrawalAlert.show();
    } // 탈퇴 버튼

    private class LeavingTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(owner_Info.this,
                    "Bye Bye..", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "2ventWithdrawal_owner.php";

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
                finish();
                Intent intent_Leavingdone = new Intent(owner_Info.this, activity_User_Login.class);
                startActivity(intent_Leavingdone);
                Toast.makeText(owner_Info.this, "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(owner_Info.this, "Withdrawal Error", Toast.LENGTH_SHORT).show();
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

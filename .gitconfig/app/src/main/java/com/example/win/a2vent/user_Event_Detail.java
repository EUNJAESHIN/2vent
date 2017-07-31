package com.example.win.a2vent;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.UserEventDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EUNJAESHIN on 2017-07-27.
 * 이벤트 상세정보 부분
 */

public class user_Event_Detail extends AppCompatActivity {
    private String TAG = "getEventDetail";
    UserEventDetailBinding binding_UserDetail;
    GetEventInfo getEventInfo;
    PutEntry putEntry;
    int event_number, event_type;
    String com_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_UserDetail = DataBindingUtil.setContentView(this, R.layout.user_event_detail);

        Intent intent_getEventinfo = getIntent();
        event_number = intent_getEventinfo.getExtras().getInt("event_number");
        // Intent에 담긴 event_number값 받기

        getEventInfo = new GetEventInfo();
        putEntry = new PutEntry();
        getEventInfo.execute(Integer.toString(event_number));
    }

    public void onClick_participation(View v) {
        if (event_type == 0) { // 0:응모형 1:결제형
            putEntry.execute(Integer.toString(event_number),
                    GlobalData.getUserID(),
                    GlobalData.getUserName(),
                    GlobalData.getUserAddr(),
                    GlobalData.getUserBirth(),
                    GlobalData.getUserSex(),
                    GlobalData.getUserPhone(),
                    Integer.toString(event_type),
                    com_number);
        } else if (event_type == 1) {

        }
        //TODO : 응모나 결제시 entry에 DB값 입력하는데 유저정보 받아와야 함
    }

    private class GetEventInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            String serverURL = "2ventGetEventOnItemInfo.php";

            try {
                ServerConnector serverConnector = new ServerConnector(serverURL);

                serverConnector.addPostData("event_number", params[0]);

                serverConnector.addDelimiter();
                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d("DB", "GetEventInfo Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response - " + result);
            if (result == null) {

            } else {
                setEventDetail(result);
            }
        }

    } // EventDB 받는 AsyncTask

    public void setEventDetail(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("EventInfo");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String event_name = item.getString("event_name");
                event_type = item.getInt("event_type");
                String event_URI = item.getString("event_URI");
                String event_price = item.getString("event_price");
                String event_dis_price = item.getString("event_dis_price");
                String event_people = item.getString("event_people");
                String event_startday = item.getString("event_startday");
                String event_endday = item.getString("event_endday");
                String event_starttime = item.getString("event_starttime");
                String event_endtime = item.getString("event_endtime");
                int event_payment = item.getInt("event_payment");
                int event_target = item.getInt("event_target");
                int event_minage = item.getInt("event_minage");
                int event_maxage = item.getInt("event_maxage");
                int event_sex = item.getInt("event_sex");
                String event_area = item.getString("event_area");
                com_number = item.getString("com_number");

                Picasso.with(this).load(GlobalData.getURL() + event_URI)
                        .placeholder(R.drawable.event_default).into(binding_UserDetail.ivDetail);
                binding_UserDetail.tvDetail1.append(event_name);
                binding_UserDetail.tvDetail2.append(event_price);
                binding_UserDetail.tvDetail2.setPaintFlags
                        (binding_UserDetail.tvDetail2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding_UserDetail.tvDetail3.append(event_dis_price);
                binding_UserDetail.tvDetail4.append(event_startday + " " + event_starttime +
                        " ~\n                  " + event_endday + " " + event_endtime);

                //target 설정 했을시
                if (event_target == 0) {

                } else if (event_target == 1) {
                    binding_UserDetail.tvDetail5.setVisibility(View.VISIBLE);
                    binding_UserDetail.tvDetail6.setVisibility(View.VISIBLE);
                    binding_UserDetail.tvDetail7.setVisibility(View.VISIBLE);
                    binding_UserDetail.tvDetail5.append
                            ("(" + event_minage + "세 ~ " + event_maxage + "세) ");
                    if (event_sex == 0) {
                        binding_UserDetail.tvDetail6.append("여성");
                    } else if (event_sex == 1) {
                        binding_UserDetail.tvDetail6.append("남성");
                    }
                    binding_UserDetail.tvDetail7.append(event_area);
                }

                binding_UserDetail.tvDetail10.append(event_people);

                //type이 응모형인지 결제형 이벤트인지 구분해서 버튼 텍스트 변경
                if (event_type == 0) {
                    binding_UserDetail.btDetail.setText("응모");
                } else if (event_type == 1) {
                    binding_UserDetail.btDetail.setText("결제");
                }
            }

        } catch (JSONException e) {
            Log.d(TAG, "setEventDetail Error : ", e);
        }
    } // 이벤트 상세정보 JSON 데이터를 텍스트뷰에 표시

    private class PutEntry extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            String serverURL = "2ventAddEntry.php";

            try {
                ServerConnector serverConnector = new ServerConnector(serverURL);

                serverConnector.addPostData("event_number", params[0]);
                serverConnector.addPostData("id", params[1]);
                serverConnector.addPostData("entry_name", params[2]);
                serverConnector.addPostData("entry_addr", params[3]);
                serverConnector.addPostData("entry_birthday", params[4]);
                serverConnector.addPostData("entry_sex", params[5]);
                serverConnector.addPostData("entry_phone", params[6]);
                serverConnector.addPostData("entry_type", params[7]);
                serverConnector.addPostData("com_number", params[8]);

                serverConnector.addDelimiter();
                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d("DB", "PutEntry Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("echo", result);

            if (result.equals("성공")) {
                Toast.makeText(user_Event_Detail.this, "응모/결제 완료", Toast.LENGTH_SHORT).show();
            } else if (result.equals("중복 에러")) {
                Toast.makeText(user_Event_Detail.this, "이미 참여한 이벤트입니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(user_Event_Detail.this, "응모/결제 오류", Toast.LENGTH_SHORT).show();
            }
        }

    } //응모형일때 이벤트 참여시 사용자 데이터 보냄

    @Override
    protected void onDestroy() {
        if (getEventInfo != null) {
            getEventInfo.cancel(true);
        } else if (putEntry != null) {
            putEntry.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (getEventInfo != null) {
            getEventInfo.cancel(true);
        } else if (putEntry != null) {
            putEntry.cancel(true);
        }
        super.onPause();
    }
}

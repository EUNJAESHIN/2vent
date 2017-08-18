package com.example.win.a2vent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.ActivityUserEventDetailsInfoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.win.a2vent.Activity_User_Login.actList;
import static com.example.win.a2vent.Activity_User_Login.toast;

/**
 * Created by EUNJAESHIN on 2017-07-27.
 * 이벤트 상세정보 부분
 */

public class Activity_User_Event_Details_Info extends AppCompatActivity {
    private String TAG = "getEventDetail";

    private boolean flagRegisterReceiver = false;

    ActivityUserEventDetailsInfoBinding binding_UserDetail;
    Context mContext;
    RecyclerView.Adapter rAdapter_image;
    ArrayList<User_Details_Item> image_All;
    GetEventInfo getEventInfo; GetImageURI getImageURI; PutEntry putEntry;
    AlertDialog dialog; AlertDialog.Builder builder;
    int event_number, event_type;
    String com_number, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        binding_UserDetail = DataBindingUtil.setContentView(this, R.layout.activity_user_event_details_info);

        mContext = getApplicationContext();
        Intent intent_getEventinfo = getIntent();
        event_number = intent_getEventinfo.getExtras().getInt("event_number");
        // Intent에 담긴 event_number값 받기
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!flagRegisterReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.example.win.a2vent.GetURI_Receiver");
            mContext.registerReceiver(broadcastReceiver, filter);
            flagRegisterReceiver = true;
        }
        getEventInfo = new GetEventInfo();
        getEventInfo.execute(Integer.toString(event_number));
    }

    public void onClick_participation(View v) {
        putEntry = new PutEntry();

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
            toast = Toast.makeText(Activity_User_Event_Details_Info.this, "결제 미구현", Toast.LENGTH_SHORT);
            toast.show();
            // 결제 부분 미구현
        }
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
                Activity_User_Event_Details_Info.this.result = result;
                getImageURI = new GetImageURI(getApplicationContext());
                getImageURI.execute(Integer.toString(event_number), "0", "1", "");
            }
        }

    } // EventDB 받는 AsyncTask

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String value = intent.getExtras().getString("finish");
            setEventDetail(result, value);
        }
    };

    public void setEventDetail(String result, String value) {
        image_All = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("EventInfo");
            JSONObject jsonObject2 = new JSONObject(value);
            JSONArray jsonArray2 = jsonObject2.getJSONArray("EventImage");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                for (int j = 0; j < jsonArray2.length(); j++) {
                    JSONObject item2 = jsonArray2.getJSONObject(j);

                    String event_URI = item2.getString("event_URI");
                    image_All.add(new User_Details_Item(event_URI));
                }

                String event_name = item.getString("event_name");
                event_type = item.getInt("event_type");
                String event_content = item.getString("event_content");
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

                binding_UserDetail.tvDetail1.append(event_name);
                binding_UserDetail.tvDetail2.append(event_price);
                binding_UserDetail.tvDetail2.setPaintFlags
                        (binding_UserDetail.tvDetail2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding_UserDetail.tvDetail3.append(event_dis_price);
                binding_UserDetail.tvDetail4.append(event_startday + " " + event_starttime +
                        " ~\n                  " + event_endday + " " + event_endtime);
                binding_UserDetail.tvDetail5.append(event_content);

                //target 설정 했을시
                if (event_target == 0) {

                } else if (event_target == 1) {
                    binding_UserDetail.tvDetail6.setVisibility(View.VISIBLE);
                    binding_UserDetail.tvDetail7.setVisibility(View.VISIBLE);
                    binding_UserDetail.tvDetail8.setVisibility(View.VISIBLE);
                    binding_UserDetail.tvDetail6.append
                            ("(" + event_minage + "세 ~ " + event_maxage + "세) ");
                    if (event_sex == 0) {
                        binding_UserDetail.tvDetail7.append("여성");
                    } else if (event_sex == 1) {
                        binding_UserDetail.tvDetail7.append("남성");
                    }
                    binding_UserDetail.tvDetail8.append(event_area);
                }

                binding_UserDetail.tvDetail9.append(event_people);

                //type이 응모형인지 결제형 이벤트인지 구분해서 버튼 텍스트 변경
                if (event_type == 0) {
                    binding_UserDetail.btDetail.setText("응모");
                } else if (event_type == 1) {
                    binding_UserDetail.btDetail.setText("결제");
                }
            }

            rAdapter_image = new User_Details_Adapter(image_All, mContext);
            binding_UserDetail.rviewDetail.setAdapter(rAdapter_image);
            rAdapter_image.notifyDataSetChanged();

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
            Log.d("PutEntry PostExecute", "response - " + result);

            if (result.equals("성공")) {
                finish();
                Intent intent_EventOK = new Intent(Activity_User_Event_Details_Info.this,
                        Activity_User_Event_Check.class);
                intent_EventOK.putExtra("result", result); // result값을 intent로 보내준다
                startActivity(intent_EventOK);
            } else if (result.equals("남은 자리 없음")) {
                finish();
                Intent intent_EventOK = new Intent(Activity_User_Event_Details_Info.this,
                        Activity_User_Event_Check.class);
                intent_EventOK.putExtra("result", result);
                startActivity(intent_EventOK);
            } else if (result.equals("중복 에러")) {
                finish();
                Intent intent_EventOK = new Intent(Activity_User_Event_Details_Info.this,
                        Activity_User_Event_Check.class);
                intent_EventOK.putExtra("result", result);
                startActivity(intent_EventOK);
            } else {
                finish();
                Intent intent_EventOK = new Intent(Activity_User_Event_Details_Info.this,
                        Activity_User_Event_Check.class);
                intent_EventOK.putExtra("result", result);
                startActivity(intent_EventOK);
            }

        }

    } // 응모형일때 이벤트 참여시 사용자 데이터 보냄

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
        if (flagRegisterReceiver) {
            mContext.unregisterReceiver(broadcastReceiver);
            flagRegisterReceiver = false;
        }
        super.onPause();
    }
}

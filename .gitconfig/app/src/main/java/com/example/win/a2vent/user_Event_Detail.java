package com.example.win.a2vent;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.UserEventDetailBinding;
import com.squareup.picasso.Picasso;

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

public class user_Event_Detail extends AppCompatActivity {

    UserEventDetailBinding binding_UserDetail;
    getEventInfo getEventInfo;
    int event_number, event_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_UserDetail = DataBindingUtil.setContentView(this, R.layout.user_event_detail);

        Intent intent_getEventinfo = getIntent();
        event_number = intent_getEventinfo.getExtras().getInt("event_number");

        getEventInfo = new getEventInfo();
        getEventInfo.execute(Integer.toString(event_number));
    }

    public void onClick_participation(View v) {
        if (event_type == 0) { // 0:응모형 1:결제형
            Toast.makeText(this, "응모 성공", Toast.LENGTH_SHORT).show();
        } else if (event_type == 1) {
            Toast.makeText(this, "결제 성공", Toast.LENGTH_SHORT).show();
        }
    }

    private class getEventInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            StringBuilder sb = null;
            try {
                URL url = new URL(GlobalData.getURL() + "2ventGetEventOnItemInfo.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF8"));
                writer.write("event_number=" + args[0]);
                writer.flush();
                writer.close();
                os.close();

                httpURLConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF8")); //캐릭터셋 설정

                sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

            } else {
                setEventDetail(result);
            }
        }

    }

    public void setEventDetail(String s) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
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
            e.printStackTrace();
        }
    }
}

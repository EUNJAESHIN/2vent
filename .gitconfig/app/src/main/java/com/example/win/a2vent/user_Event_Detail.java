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
    int event_number;

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
        Toast.makeText(this, "응모 성공", Toast.LENGTH_SHORT).show();
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
                String event_URI = item.getString("event_URI");
                String event_price = item.getString("event_price");
                String event_dis_price = item.getString("event_dis_price");
                String event_people = item.getString("event_people");
                String event_startday = item.getString("event_startday");
                String event_endday = item.getString("event_endday");
                String event_starttime = item.getString("event_starttime");
                String event_endtime = item.getString("event_endtime");
//                String event_payment = item.getString("event_payment");

                Picasso.with(this).load(GlobalData.getURL() + event_URI)
                        .placeholder(R.drawable.event_default).into(binding_UserDetail.ivDetail);
                binding_UserDetail.tvDetail1.append(event_name);
                binding_UserDetail.tvDetail2.append(event_price);
                binding_UserDetail.tvDetail2.setPaintFlags
                        (binding_UserDetail.tvDetail2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding_UserDetail.tvDetail3.append(event_dis_price);
                binding_UserDetail.tvDetail4.append(event_startday + " " + event_starttime +
                        " ~\n                   " + event_endday + " " + event_endtime);

                binding_UserDetail.tvDetail10.append(event_people);

//                if (event_type == 0) {
//                    bt_do.setText("응모하기");
//                } else {
//                    bt_do.setText("결제하기");
//                }
//
//                infoImageDown = new user_iteminfo.infoImageDown();
//                infoImageDown.execute(event_URI);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

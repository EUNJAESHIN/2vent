package com.example.win.a2vent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by win on 2017-07-26.
 */

public class Activity_Owner_Event_Details_Info extends AppCompatActivity {

    private final static String TAG = "테스트";

    private String mEvent_number;

    private TextView tvType, tvStore, tvFixedPrice, tvDiscount, tvLimitPersons, tvStartDate, tvStartTime, tvEndDate, tvEndTime, tvEventName, tvMinimumAge, tvMaximumAge, tvSex, tvLocation, tvPayment;

    private ImageView imgContents;

    private Switch swConditions;

    private Button btnEntryList;

    private GetData getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_event_details_info);

        Intent intent = getIntent();
        mEvent_number = intent.getStringExtra("event_number");

        Log.d(TAG, "event_number: " + mEvent_number);

        tvType = (TextView) findViewById(R.id.tvType);
        tvStore = (TextView) findViewById(R.id.tvStore);
        tvFixedPrice = (TextView) findViewById(R.id.tvFixedPrice);
        tvDiscount = (TextView) findViewById(R.id.tvDiscount);
        tvLimitPersons = (TextView) findViewById(R.id.tvLimitPersons);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        tvEventName = (TextView) findViewById(R.id.tvEventName);
        tvMinimumAge = (TextView) findViewById(R.id.tvMinimumAge);
        tvMaximumAge = (TextView) findViewById(R.id.tvMaximumAge);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvPayment = (TextView) findViewById(R.id.tvPayment);

        imgContents = (ImageView) findViewById(R.id.imgContents);

        swConditions = (Switch) findViewById(R.id.swConditions);

        btnEntryList = (Button) findViewById(R.id.btnEntryList);
        btnEntryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Owner_Event_Details_Info.this, Activity_Owner_Entry_List.class);
                intent.putExtra("event_number" , mEvent_number);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData = new GetData();
        getData.execute(String.valueOf(mEvent_number));
    }

    @Override
    protected void onPause() {
        Activity_Owner_Event_Details_Info.this.finish();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ServerConnector serverConnector = new ServerConnector("2ventGetStoreEventInfo.php");

                serverConnector.addPostData("event_number", params[0]);
                serverConnector.addDelimiter();

                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (NullPointerException e) {
                return new String("NullPoint: " + e.getMessage());
            } catch (Exception e) {
                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response - " + result);

            if (result == null) {

            } else {
                addItem(result);
            }
        }

        private void addItem(String result) {
            ArrayList<Owner_Event_Details_Info_Item> arrayList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("EventDetailsInfo");

                JSONObject item;
                String event_name;
                String event_type;
                String event_stats;
                String event_URI;
                String event_price;
                String event_dis_price;
                String event_people;
                String event_startday;
                String event_endday;
                String event_starttime;
                String event_endtime;
                String event_payment;
                String event_target;
                String event_minage;
                String event_maxage;
                String event_sex;
                String event_area;
                String com_name;

                for (int i = 0; i < jsonArray.length(); i++) {
                    item = jsonArray.getJSONObject(i);

                    event_name = item.getString("event_name");
                    event_type = item.getString("event_type");
                    event_stats = item.getString("event_stats");
                    event_URI = item.getString("event_URI");
                    event_price = item.getString("event_price");
                    event_dis_price = item.getString("event_dis_price");
                    event_people = item.getString("event_people");
                    event_startday = item.getString("event_startday");
                    event_endday = item.getString("event_endday");
                    event_starttime = item.getString("event_starttime");
                    event_endtime = item.getString("event_endtime");
                    event_payment = item.getString("event_payment");
                    event_target = item.getString("event_target");
                    event_minage = item.getString("event_minage");
                    event_maxage = item.getString("event_maxage");
                    event_sex = item.getString("event_sex");
                    event_area = item.getString("event_area");
                    com_name = item.getString("com_name");

                    arrayList.add(new Owner_Event_Details_Info_Item(event_name, event_type, event_stats, event_URI,
                            event_price, event_dis_price, event_people, event_startday, event_endday, event_starttime,
                            event_endtime, event_payment, event_target, event_minage, event_maxage, event_sex, event_area,
                            com_name));
                }

                updateTextView(arrayList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void updateTextView(ArrayList<Owner_Event_Details_Info_Item> arrayList) {
            if (arrayList.size() != 0) {

                String tmp_year, tmp_month, tmp_day, tmp_hour, tmp_min;
                StringTokenizer tokenDay, tokenTime;

                if (arrayList.get(0).getEvent_type().equals("0")) {
                    tvType.setText("응모형");
                    TextView tv = (TextView) findViewById(R.id.tv19);
                    tv.setVisibility(View.GONE);
                    tvPayment.setVisibility(View.GONE);
                    tvPayment.setText("");
                } else if (arrayList.get(0).getEvent_type().equals("1")) {
                    TextView tv = (TextView) findViewById(R.id.tv19);
                    tv.setVisibility(View.VISIBLE);
                    tvType.setText("결제형");
                    tvPayment.setVisibility(View.VISIBLE);
                    if (arrayList.get(0).getEvent_payment().equals("0")) {
                        tvPayment.setText("현금 결제");
                    } else if (arrayList.get(0).getEvent_payment().equals("1")) {
                        tvPayment.setText("카드 결제");
                    }

                }
                tvStore.setText(arrayList.get(0).getCom_name());
                tvFixedPrice.setText(new DecimalFormat("###,###").format(Long.parseLong(arrayList.get(0).getEvent_price())));
                tvDiscount.setText(new DecimalFormat("###,###").format(Long.parseLong(arrayList.get(0).getEvent_dis_price())));
                tvLimitPersons.setText(new DecimalFormat("###,###").format(Long.parseLong(arrayList.get(0).getEvent_people())));

                tokenDay = new StringTokenizer(arrayList.get(0).getEvent_startday(), "-");
                tokenTime = new StringTokenizer(arrayList.get(0).getEvent_starttime(), ":");
                try {
                    tmp_year = tokenDay.nextToken();
                    tmp_month = tokenDay.nextToken();
                    tmp_day = tokenDay.nextToken();
                    tmp_hour = tokenTime.nextToken();
                    tmp_min = tokenTime.nextToken();

                    tvStartDate.setText(tmp_year.concat("년 " ).concat(tmp_month).concat("월 ").concat(tmp_day).concat("일"));
                    tvStartTime.setText(tmp_hour.concat("시 ").concat(tmp_min).concat("분"));
                } catch (NoSuchElementException e) {

                }

                tokenDay = new StringTokenizer(arrayList.get(0).getEvent_endday(), "-");
                tokenTime = new StringTokenizer(arrayList.get(0).getEvent_endtime(), ":");
                try {
                    tmp_year = tokenDay.nextToken();
                    tmp_month = tokenDay.nextToken();
                    tmp_day = tokenDay.nextToken();
                    tmp_hour = tokenTime.nextToken();
                    tmp_min = tokenTime.nextToken();

                    tvEndDate.setText(tmp_year.concat("년 " ).concat(tmp_month).concat("월 ").concat(tmp_day).concat("일"));
                    tvEndTime.setText(tmp_hour.concat("시 ").concat(tmp_min).concat("분"));
                } catch (NoSuchElementException e) {

                }

                tvEventName.setText(arrayList.get(0).getEvent_name());

                Picasso.with(getApplicationContext()).load(GlobalData.getURL() + arrayList.get(0).getEvent_URI())
                        .placeholder(R.drawable.event_default).into(imgContents);

                if (arrayList.get(0).getEvent_target().equals("0")) {
                    swConditions.setChecked(false);
                    TextView tv;
                    tv = (TextView) findViewById(R.id.tvAge);
                    tv.setEnabled(false);
                    tv = (TextView) findViewById(R.id.tvAge1);
                    tv.setEnabled(false);
                    tv = (TextView) findViewById(R.id.tv17);
                    tv.setEnabled(false);
                    tv = (TextView) findViewById(R.id.tv18);
                    tv.setEnabled(false);
                    tvMinimumAge.setEnabled(false);
                    tvMinimumAge.setText("");
                    tvMaximumAge.setEnabled(false);
                    tvMaximumAge.setText("");
                    tvSex.setEnabled(false);
                    tvSex.setText("");
                    tvLocation.setEnabled(false);
                    tvLocation.setText("");
                } else if (arrayList.get(0).getEvent_target().equals("1")) {
                    swConditions.setChecked(true);
                    TextView tv;
                    tv = (TextView) findViewById(R.id.tvAge);
                    tv.setEnabled(true);
                    tv = (TextView) findViewById(R.id.tvAge1);
                    tv.setEnabled(true);
                    tv = (TextView) findViewById(R.id.tv17);
                    tv.setEnabled(true);
                    tv = (TextView) findViewById(R.id.tv18);
                    tv.setEnabled(true);
                    tvMinimumAge.setEnabled(true);
                    tvMinimumAge.setText(arrayList.get(0).getEvent_minage());
                    tvMaximumAge.setEnabled(true);
                    tvMaximumAge.setText(arrayList.get(0).getEvent_maxage());
                    tvSex.setEnabled(true);
                    if (arrayList.get(0).getEvent_sex().equals("0")) {
                        tvSex.setText("여성만 참여가능");
                    } else if (arrayList.get(0).getEvent_sex().equals("1")) {
                        tvSex.setText("남성만 참여가능");
                    } else if (arrayList.get(0).getEvent_sex().equals("2")) {
                        tvSex.setText("남녀 모두 참여가능");
                    }
                    tvLocation.setEnabled(true);
                    tvLocation.setText(arrayList.get(0).getEvent_area());
                }
            } else {
                Toast.makeText(Activity_Owner_Event_Details_Info.this,
                        "일시적인 오류가 발생했습니다", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Activity_Owner_Event_Details_Info.this.onBackPressed();
                    }
                }, 2000);
            }
        }
    }
}

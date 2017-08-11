package com.example.win.a2vent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.example.win.a2vent.databinding.ActivityOwnerEventDetailsInfoBinding;

/**
 * Created by win on 2017-07-26.
 */

public class Activity_Owner_Event_Details_Info extends AppCompatActivity {

    private final static String TAG = "테스트";

    private String mEvent_number;
    private String mResult;

    private TextView tvType, tvStore, tvFixedPrice, tvDiscount, tvLimitPersons, tvStartDate,
            tvStartTime, tvEndDate, tvEndTime, tvEventName, tvMinimumAge, tvMaximumAge, tvSex, tvLocation, tvPayment;

    private Switch swConditions;

    private Button btnEntryList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;

    private ActivityOwnerEventDetailsInfoBinding binding;

    private boolean flagRegisterReceiver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner_event_details_info);

        Intent intent = getIntent();
        mEvent_number = intent.getStringExtra("event_number");

        Log.d(TAG, "event_number: " + mEvent_number);

        tvType = binding.tvType;
        tvStore = binding.tvStore;
        tvFixedPrice = binding.tvFixedPrice;
        tvDiscount = binding.tvDiscount;
        tvLimitPersons = binding.tvLimitPersons;
        tvStartDate = binding.tvStartDate;
        tvStartTime = binding.tvStartTime;
        tvEndDate = binding.tvEndDate;
        tvEndTime = binding.tvEndTime;
        tvEventName = binding.tvEventName;
        tvMinimumAge = binding.tvMinimumAge;
        tvMaximumAge = binding.tvMaximumAge;
        tvSex = binding.tvSex;
        tvLocation = binding.tvLocation;
        tvPayment = binding.tvPayment;

        mRecyclerView = binding.rvContents;

        swConditions = binding.swConditions;

        btnEntryList = binding.btnEntryList;
        btnEntryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Owner_Event_Details_Info.this, Activity_Owner_Entry_List.class);
                intent.putExtra("event_number", mEvent_number);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!flagRegisterReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalData.GET_URI_RECEIVER);
            registerReceiver(broadcastReceiver, filter);
            flagRegisterReceiver = true;

            Log.d(TAG, "register receiver");
        }

        new GetData().execute(String.valueOf(mEvent_number));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (flagRegisterReceiver) {
            unregisterReceiver(broadcastReceiver);
            flagRegisterReceiver = false;

            Log.d(TAG, "unregister receiver");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Activity_Owner_Event_Details_Info.this.finish();
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
                mResult = result;
                new GetImageURI(getApplicationContext()).execute(mEvent_number, "0", "1");
            }
        }
    }

    private void addItem(String result1, String result2) {

        try {
            JSONObject jsonObject = new JSONObject(result1);
            JSONArray jsonArray = jsonObject.getJSONArray("EventDetailsInfo");

            JSONObject item;
            String event_name = null;
            String event_type = null;
            String event_content = null;
            String event_price = null;
            String event_dis_price = null;
            String event_people = null;
            String event_startday = null;
            String event_endday = null;
            String event_starttime = null;
            String event_endtime = null;
            String event_payment = null;
            String event_target = null;
            String event_minage = null;
            String event_maxage = null;
            String event_sex = null;
            String event_area = null;
            String com_name = null;

            if (jsonArray.length() > 0) {
                item = jsonArray.getJSONObject(0);

                event_name = item.getString("event_name");
                event_type = item.getString("event_type");
                event_content = item.getString("event_content");
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
            }

            jsonObject = new JSONObject(result2);
            jsonArray = jsonObject.getJSONArray("EventImage");

            String event_URI;
            ArrayList<Owner_Event_Details_Info_Item> arrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                item = jsonArray.getJSONObject(i);

                event_URI = item.getString("event_URI");

                arrayList.add(new Owner_Event_Details_Info_Item(event_URI));
            }

            mRecyclerViewAdapter = new Owner_Event_Details_Info_Adapter(arrayList, Activity_Owner_Event_Details_Info.this);

            mRecyclerView.setAdapter(mRecyclerViewAdapter);

            mRecyclerViewAdapter.notifyDataSetChanged();

            updateTextView(event_name, event_type, event_content, event_price, event_dis_price, event_people, event_startday, event_starttime,
                    event_endday, event_endtime, event_payment, event_target, event_minage, event_maxage, event_sex, event_area, com_name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateTextView(String name, String type, String content, String price, String dis_price,
                                String people, String startday, String endday, String starttime, String endtime, String payment,
                                String target, String minage, String maxage, String sex, String area, String com_name) {
        try {

            String tmp_year, tmp_month, tmp_day, tmp_hour, tmp_min;
            StringTokenizer tokenDay, tokenTime;

            if (type.equals("0")) {
                tvType.setText("응모형");
                TextView tv = (TextView) findViewById(R.id.tv19);
                tv.setVisibility(View.GONE);
                tvPayment.setVisibility(View.GONE);
                tvPayment.setText("");
            } else if (type.equals("1")) {
                TextView tv = (TextView) findViewById(R.id.tv19);
                tv.setVisibility(View.VISIBLE);
                tvType.setText("결제형");
                tvPayment.setVisibility(View.VISIBLE);
                if (payment.equals("0")) {
                    tvPayment.setText("현금 결제");
                } else if (payment.equals("1")) {
                    tvPayment.setText("카드 결제");
                }

            }
            tvStore.setText(com_name);
            tvFixedPrice.setText(new DecimalFormat("###,###").format(Long.parseLong(price)));
            tvDiscount.setText(new DecimalFormat("###,###").format(Long.parseLong(dis_price)));
            tvLimitPersons.setText(new DecimalFormat("###,###").format(Long.parseLong(people)));

            tokenDay = new StringTokenizer(startday, "-");
            tokenTime = new StringTokenizer(starttime, ":");
            try {
                tmp_year = tokenDay.nextToken();
                tmp_month = tokenDay.nextToken();
                tmp_day = tokenDay.nextToken();
                tmp_hour = tokenTime.nextToken();
                tmp_min = tokenTime.nextToken();

                tvStartDate.setText(tmp_year.concat("년 ").concat(tmp_month).concat("월 ").concat(tmp_day).concat("일"));
                tvStartTime.setText(tmp_hour.concat("시 ").concat(tmp_min).concat("분"));
            } catch (NoSuchElementException e) {

            }

            tokenDay = new StringTokenizer(endday, "-");
            tokenTime = new StringTokenizer(endtime, ":");
            try {
                tmp_year = tokenDay.nextToken();
                tmp_month = tokenDay.nextToken();
                tmp_day = tokenDay.nextToken();
                tmp_hour = tokenTime.nextToken();
                tmp_min = tokenTime.nextToken();

                tvEndDate.setText(tmp_year.concat("년 ").concat(tmp_month).concat("월 ").concat(tmp_day).concat("일"));
                tvEndTime.setText(tmp_hour.concat("시 ").concat(tmp_min).concat("분"));
            } catch (NoSuchElementException e) {

            }

            tvEventName.setText(name);

            //Picasso.with(getApplicationContext()).load(GlobalData.getURL() + arrayList.get(0).getEvent_URI())
            //        .placeholder(R.drawable.event_default).into(imgContents);

            if (target.equals("0")) {
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
            } else if (target.equals("1")) {
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
                tvMinimumAge.setText(minage);
                tvMaximumAge.setEnabled(true);
                tvMaximumAge.setText(maxage);
                tvSex.setEnabled(true);
                if (sex.equals("0")) {
                    tvSex.setText("여성만 참여가능");
                } else if (sex.equals("1")) {
                    tvSex.setText("남성만 참여가능");
                } else if (sex.equals("2")) {
                    tvSex.setText("남녀 모두 참여가능");
                }
                tvLocation.setEnabled(true);
                tvLocation.setText(area);
            }
        } catch (NullPointerException e) {
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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String value = intent.getExtras().getString("finish");

            if (value == null) {

            } else {
                addItem(mResult, value);
            }
        }
    };
}

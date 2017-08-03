package com.example.win.a2vent;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.win.a2vent.databinding.ActivityOwnerEntryListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by win on 2017-08-01.
 */

public class Activity_Owner_Entry_List extends AppCompatActivity {

    private final String TAG = "테스트";

    private Context mContext;

    private TextView tvEventName;
    private TextView tvToday;

    private Button btnDetailsEvent;
    private ImageButton btnSearch;

    private ActivityOwnerEntryListBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;

    private String mEvent_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner_entry_list);

        Intent intent = getIntent();
        mEvent_number = intent.getStringExtra("event_number");

        Log.d(TAG, "event_number: " + mEvent_number);

        mContext = getApplicationContext();
        recyclerView = binding.recyclerView;
        tvEventName = binding.tvEventName;
        tvToday = binding.tvToday;
        btnDetailsEvent = binding.btnDetailsEvent;
        btnSearch = binding.btnSearch;

        btnDetailsEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Owner_Entry_List.this, Activity_Owner_Event_Details_Info.class);
                intent.putExtra("event_number" , mEvent_number);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        GetData getData = new GetData();
        getData.execute(mEvent_number);
    }

    @Override
    protected void onPause() {
        Activity_Owner_Entry_List.this.finish();

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
                ServerConnector serverConnector = new ServerConnector("2ventStoreEntryList.php");

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

        private void addItem(String reuslt) {
            ArrayList<Owner_Entry_Item> arrayList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(reuslt);
                JSONArray jsonArray = jsonObject.getJSONArray("StoreEntryList");

                JSONObject item;
                String id;
                String entry_name;
                String entry_addr;
                String entry_sex;
                String entry_phone;
                String is_entry;
                String event_name = null;
                String strSex = null;

                for (int i = 0; i < jsonArray.length(); i++) {
                    item = jsonArray.getJSONObject(i);

                    id = item.getString("id");
                    entry_name = item.getString("entry_name");
                    entry_addr = item.getString("entry_addr");
                    entry_sex = item.getString("entry_sex");
                    entry_phone = item.getString("entry_phone");
                    is_entry = item.getString("is_entry");
                    event_name = item.getString("event_name");

                    if (entry_sex.equals("0")) {
                        strSex = "여";
                    } else if (entry_sex.equals("1")) {
                        strSex = "남";
                    }

                    try {
                        entry_name = entry_name.replace(String.valueOf(entry_name.charAt(1)), "*");
                    } catch (StringIndexOutOfBoundsException e) {

                    }

                    try {
                        if (entry_phone.length() < 11) {
                            entry_phone = entry_phone.replace(entry_phone.substring(3, 6), "-****-");
                        } else {
                            entry_phone = entry_phone.replace(entry_phone.substring(3, 7), "-****-");
                        }
                    } catch (StringIndexOutOfBoundsException e) {

                    }


                    arrayList.add(new Owner_Entry_Item(i+1, id, entry_name, entry_addr, strSex, entry_phone, is_entry));
                }

                recyclerViewAdapter = new Owner_Entry_Adapter(arrayList, Activity_Owner_Entry_List.this);

                recyclerView.setAdapter(recyclerViewAdapter);

                recyclerViewAdapter.notifyDataSetChanged();

                updateView(event_name);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void updateView(String event_name) {
            String today, strYear, strMonth, strDay;
            Calendar cal = Calendar.getInstance();
            int year, month, day;

            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            day = cal.get(Calendar.DAY_OF_MONTH);

            strYear = String.valueOf(year);

            if (month < 10) {
                strMonth = "0".concat(String.valueOf(month));
            } else {
                strMonth = String.valueOf(month);
            }

            if (day < 10) {
                strDay = "0".concat(String.valueOf(day));
            } else {
                strDay = String.valueOf(day);
            }

            today = strYear.concat("-").concat(strMonth).concat("-").concat(strDay);

            tvEventName.setText(event_name);
            tvToday.setText(today);
        }
    }
}

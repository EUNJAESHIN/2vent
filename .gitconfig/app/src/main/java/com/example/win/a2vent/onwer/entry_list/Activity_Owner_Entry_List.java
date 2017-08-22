package com.example.win.a2vent.onwer.entry_list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.win.a2vent.R;
import com.example.win.a2vent.onwer.details_info.Activity_Owner_Event_Details_Info;
import com.example.win.a2vent.util.ServerConnector;
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

    private SearchView searchView;

    private ActivityOwnerEntryListBinding binding;
    private RecyclerView recyclerView;
    private Owner_Entry_Adapter entryAdapter;

    private String mEvent_number;
    private boolean flagRegisterReceiver = false;

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
        searchView = binding.searchEntry;

        btnDetailsEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Owner_Entry_List.this, Activity_Owner_Event_Details_Info.class);
                intent.putExtra("event_number" , mEvent_number);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!flagRegisterReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.example.win.a2vent.Activity_Owner_Entry_List_Receiver");
            mContext.registerReceiver(receiver, filter);
            flagRegisterReceiver = true;
        }

        resume();
    }

    public void resume() {
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

                entryAdapter = new Owner_Entry_Adapter(arrayList, Activity_Owner_Entry_List.this, mEvent_number);

                recyclerView.setAdapter(entryAdapter);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        entryAdapter.getFilter().filter(s);
                        return false;
                    }
                });

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

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getExtras().getString("input_finish");

            if (value.equals("success")) {
                resume();
            }
        }
    };
}

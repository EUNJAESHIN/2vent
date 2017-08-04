package com.example.win.a2vent;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.win.a2vent.databinding.ActivityUserEntryListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.win.a2vent.Activity_User_Login.actList;
import static com.example.win.a2vent.Activity_User_Login.toast;

/**
 * Created by EUNJAESHIN on 2017-08-03.
 * 사용자 이벤트 참여 현황 부분
 */

public class Activity_User_Entry_List extends AppCompatActivity {

    public static Context context;
    ActivityUserEntryListBinding binding_UserEntryList;
    static RecyclerView.Adapter rAdapter_UserEntryList;
    static ArrayList mCategory;
    GetMyEntry getMyEntry;
    int event_number,com_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        binding_UserEntryList = DataBindingUtil.setContentView(this, R.layout.activity_user_entry_list);
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyEntry = new GetMyEntry();
        getMyEntry.execute(GlobalData.getUserID());
    }

    private class GetMyEntry extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "2ventGetUserEntry.php";

            try {
                ServerConnector serverConnector = new ServerConnector(serverURL);

                serverConnector.addPostData("id", params[0]);

                serverConnector.addDelimiter();
                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d("DB", "GetEventInfo Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("GetMyEntry", "response - " + result);
            if (result == null) {

            } else {
                addItemInEntry(result);
            }
        }

    } // UserEntry받는 AsyncTask

    private void addItemInEntry(String result) {
        mCategory = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("MyEntry");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                int event_number = item.getInt("event_number");
                String event_name = item.getString("event_name");
                com_category = item.getInt("com_category");
                String event_price = item.getString("event_price");
                String event_dis_price = item.getString("event_dis_price");
                String event_startday = item.getString("event_startday");
                String event_endday = item.getString("event_endday");

                if (com_category == 0) {
                    mCategory.add(new User_Entry_Item(event_number, "문화", event_name, event_price,
                            event_dis_price, event_startday, event_endday));
                } else if (com_category == 1) {
                    mCategory.add(new User_Entry_Item(event_number, "외식", event_name, event_price,
                            event_dis_price, event_startday, event_endday));
                } else if (com_category == 2) {
                    mCategory.add(new User_Entry_Item(event_number, "뷰티", event_name, event_price,
                            event_dis_price, event_startday, event_endday));
                } else if (com_category == 3) {
                    mCategory.add(new User_Entry_Item(event_number, "패션", event_name, event_price,
                            event_dis_price, event_startday, event_endday));
                }
            }

            rAdapter_UserEntryList = new User_Entry_Adapter(mCategory, getApplicationContext());
            binding_UserEntryList.entryRviewContent.setAdapter(rAdapter_UserEntryList);
            rAdapter_UserEntryList.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.d("GetMyEntry", "addItemInEntry Error : ", e);
        }

    }

    @Override
    protected void onDestroy() {
        if (getMyEntry != null) {
            getMyEntry.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (getMyEntry != null) {
            getMyEntry.cancel(true);
        }
        super.onPause();
    }
}

package com.example.win.a2vent.onwer.add_store;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.win.a2vent.util.GetImageURI;
import com.example.win.a2vent.util.GlobalData;
import com.example.win.a2vent.R;
import com.example.win.a2vent.util.ServerConnector;
import com.example.win.a2vent.databinding.ActivityOwnerStoreEventListBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by win on 2017-08-01.
 */

public class Activity_Owner_Store_Event_List extends AppCompatActivity {

    private final String TAG = "테스트";

    private Context mContext;

    private ImageView imgStore;

    private TextView tvStoreName;

    private ActivityOwnerStoreEventListBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;

    private String mCom_number;

    private String mComName, mComURI;

    private String mResult;

    private boolean flagRegisterReceiver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner_store_event_list);

        Intent intent = getIntent();
        mCom_number = intent.getStringExtra("com_number");

        Log.d(TAG, "com_number: " + mCom_number);

        mContext = getApplicationContext();
        recyclerView = binding.recyclerView;
        imgStore = binding.imgStore;
        tvStoreName = binding.tvStoreName;
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetData getData = new GetData();
        getData.execute(mCom_number);

        if (!flagRegisterReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalData.GET_URI_RECEIVER);
            registerReceiver(broadcastReceiver, filter);
            flagRegisterReceiver = true;
        }
    }

    @Override
    protected void onPause() {
        if (flagRegisterReceiver) {
            unregisterReceiver(broadcastReceiver);
            flagRegisterReceiver = false;
        }

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
                ServerConnector serverConnector = new ServerConnector("2ventGetEventListOfStore.php");

                serverConnector.addPostData("com_number", params[0]);
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
                new GetImageURI(mContext).execute("0", "0", "0", mCom_number);
            }
        }
    }

    private void addItem(String result1, String result2) {
        ArrayList<Owner_Store_Event_List_Item> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject1 = new JSONObject(result1);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("EventListOfStore");
            JSONObject jsonObject2 = new JSONObject(result2);
            JSONArray jsonArray2 = jsonObject2.getJSONArray("EventMainImage");

            JSONObject item1, item2;
            String event_number;
            String event_name;
            String event_startday;
            String event_endday;
            String event_URI;

            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... params) {
                    try {
                        ServerConnector serverConnector = new ServerConnector("2ventGetCompany.php");

                        serverConnector.addPostData("com_number", params[0]);
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

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("Company");
                        JSONObject item;

                        String com_name = null;
                        String com_URI = null;

                        if (jsonArray.length() > 0) {
                            item = jsonArray.getJSONObject(0);

                            com_name = item.getString("com_name");
                            com_URI = item.getString("com_URI");
                        }

                        Picasso.with(Activity_Owner_Store_Event_List.this).load(GlobalData.getURL() + com_URI)
                                .placeholder(R.drawable.store_default).into(imgStore);
                        tvStoreName.setText(com_name);
                    } catch (JSONException e) {

                    }
                }
            }.execute(mCom_number);

            if (jsonArray1.length() == jsonArray2.length()) {
                for (int i = 0; i < jsonArray1.length(); i++) {
                    item1 = jsonArray1.getJSONObject(i);
                    item2 = jsonArray2.getJSONObject(i);

                    event_number = item1.getString("event_number");
                    event_name = item1.getString("event_name");
                    event_startday = item1.getString("event_startday");
                    event_endday = item1.getString("event_endday");
                    event_URI = item2.getString("event_URI");

                    arrayList.add(new Owner_Store_Event_List_Item(event_number, event_name, event_startday, event_endday, event_URI));
                }
            }

            recyclerViewAdapter = new Owner_Store_Event_List_Adapter(arrayList, Activity_Owner_Store_Event_List.this);

            recyclerView.addItemDecoration(new DividerItemDecoration(Activity_Owner_Store_Event_List.this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(recyclerViewAdapter);

            recyclerViewAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
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

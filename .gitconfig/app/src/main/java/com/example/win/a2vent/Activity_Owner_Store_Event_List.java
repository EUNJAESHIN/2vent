package com.example.win.a2vent;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.win.a2vent.databinding.ActivityOwnerStoreEventListBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by win on 2017-08-01.
 */

public class Activity_Owner_Store_Event_List extends AppCompatActivity{

    private final String TAG = "테스트";

    private Context mContext;

    private ImageView imgStore;

    private TextView tvStoreName;

    private ActivityOwnerStoreEventListBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;

    private String mCom_number;

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
    }

    @Override
    protected void onPause() {
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
                addItem(result);
            }
        }

        private void addItem(String result) {
            ArrayList<Owner_Store_Event_List_Item> arrayList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("EventListOfStore");

                JSONObject item;
                String event_number;
                String event_name;
                String event_URI;
                String event_startday;
                String event_endday;
                String com_name = null;
                String com_URI = null;

                for (int i = 0; i < jsonArray.length(); i++) {
                    item = jsonArray.getJSONObject(i);

                    event_number = item.getString("event_number");
                    event_name = item.getString("event_name");
                    event_URI = item.getString("event_URI");
                    event_startday = item.getString("event_startday");
                    event_endday = item.getString("event_endday");
                    com_name = item.getString("com_name");
                    com_URI = item.getString("com_URI");

                    arrayList.add(new Owner_Store_Event_List_Item(event_number, event_name, event_URI, event_startday, event_endday));
                }

                recyclerViewAdapter = new Owner_Store_Event_List_Adapter(arrayList, Activity_Owner_Store_Event_List.this);

                recyclerView.addItemDecoration(new DividerItemDecoration(Activity_Owner_Store_Event_List.this, DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(recyclerViewAdapter);

                recyclerViewAdapter.notifyDataSetChanged();

                updateView(com_name, com_URI);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void updateView(String com_name, String com_URI) {
            Picasso.with(Activity_Owner_Store_Event_List.this).load(GlobalData.getURL() + com_URI)
                    .placeholder(R.drawable.store_default).into(imgStore);
            tvStoreName.setText(com_name);
        }
    }
}

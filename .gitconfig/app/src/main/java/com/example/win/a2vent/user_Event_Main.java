package com.example.win.a2vent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.win.a2vent.databinding.UserEventMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 사용자 메인 화면
 * <p>
 * Android & PHP & MySQL 예제
 * http://webnautes.tistory.com/category/Android/DATABASE
 * 데이터 바인딩 라이브러리
 * https://developer.android.com/topic/libraries/data-binding/index.html?hl=ko
 */

public class user_Event_Main extends AppCompatActivity {
    private String TAG = "getEventDB";
    private final String TAG_JSON = "Event";
    private final String TAG_NUM = "event_number";
    private final String TAG_NAME = "event_name";
    private final String TAG_TYPE = "event_type";
    private final String TAG_URI = "event_URI";
    private final String TAG_PRICE = "event_price";
    private final String TAG_DISPRICE = "event_dis_price";
    private final String TAG_STARTDAY = "event_startday";
    private final String TAG_ENDDAY = "event_endday";

    private long backKeyPressedTime = 0;
    UserEventMainBinding binding_UserMain;
    Context mContext;
    RecyclerView.Adapter rAdapter1, rAdapter2, rAdapter3, rAdapter4, rAdapter5;
    ArrayList category_All, category_Culture, category_Meal, category_Beauty, category_Fashion;
    getEventDB getEventDB;
    int event_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_UserMain = DataBindingUtil.setContentView(this, R.layout.user_event_main);
        set_TabHost(this);

        mContext = getApplicationContext();

        getEventDB = new getEventDB();
        getEventDB.execute(GlobalData.getURL() + "2ventGetEventAll.php"); // AsyncTask 실행
    }

    public void onClick_Accountinfo(View v) {
        Intent GoUserinfo = new Intent(this, user_Info.class);
        startActivity(GoUserinfo);
    }

    public void onClick_goMap(View v) {
        Intent intent = new Intent(this, user_map.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(user_Event_Main.this,
                    "'뒤로' 버튼을 한번 더 누르면 로그아웃", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    } // 백키 2번 로그아웃

    public void set_TabHost(Activity a) {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost_usermain);
        tabHost.setup();

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("전체");
        tabSpec1.setContent(R.id.content1);
        tabSpec1.setIndicator("전체");
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("문화");
        tabSpec2.setContent(R.id.content2);
        tabSpec2.setIndicator("문화");
        tabHost.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("외식");
        tabSpec3.setContent(R.id.content3);
        tabSpec3.setIndicator("외식");
        tabHost.addTab(tabSpec3);

        TabHost.TabSpec tabSpec4 = tabHost.newTabSpec("뷰티");
        tabSpec4.setContent(R.id.content4);
        tabSpec4.setIndicator("뷰티");
        tabHost.addTab(tabSpec4);

        TabHost.TabSpec tabSpec5 = tabHost.newTabSpec("패션");
        tabSpec5.setContent(R.id.content5);
        tabSpec5.setIndicator("패션");
        tabHost.addTab(tabSpec5);

        // 탭 이동시 리스너
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId == "전체") {
                    rAdapter1.notifyDataSetChanged();
                } else if (tabId == "문화") {
                    rAdapter2.notifyDataSetChanged();
                } else if (tabId == "외식") {
                    rAdapter3.notifyDataSetChanged();
                } else if (tabId == "뷰티") {
                    rAdapter4.notifyDataSetChanged();
                } else if (tabId == "패션") {
                    rAdapter5.notifyDataSetChanged();
                }
            }
        });
    } // TabWidget 설정

    private class getEventDB extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                // URL 요청 후 응답받기

                httpURLConnection.setReadTimeout(5000); // 읽기 타임아웃
                httpURLConnection.setConnectTimeout(5000); // 연결 타임아웃
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                // * HTTP 상태코드 200 (성공), 403 (금지됨), 404 (찾을 수 없음) 등

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                /*
                InputStreamReader / OutputStreamWriter 는 바이트 스트림에서 문자 스트림으로,
                또는 문자 스트림에서 바이트 스트림으로의 변환을 제공하는 입출력 스트림이다.
                바이트를 읽어서 지정된 문자 인코딩에 따라 문자로 변환하는데 사용한다.
                 */

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                /*
                BufferedReader / BufferedWriter는 문자 입력 스트림으로부터 문자를 읽어 들이거나
                문자 출력 스트림으로 문자를 내보낼 때 버퍼링을 함으로써 문자, 문자 배열,
                문자열 라인 등을 보다 효율적으로 처리할 수 있도록 해준다.
                 */

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                };
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "getEventDB Error : ", e);
                errorString = e.toString();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response  - " + result);

            if (result == null) {

            } else {
                addItemInCategory(result);
            }
        }

    } // EventDB 받는 AsyncTask

    private void addItemInCategory(String result) {
        category_All = new ArrayList<>();
        category_Culture = new ArrayList<>();
        category_Meal = new ArrayList<>();
        category_Beauty = new ArrayList<>();
        category_Fashion = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                int event_number = item.getInt(TAG_NUM);
                String event_name = item.getString(TAG_NAME);
                event_type = item.getInt(TAG_TYPE);
                String event_URI = item.getString(TAG_URI);
                String event_price = item.getString(TAG_PRICE);
                String event_dis_price = item.getString(TAG_DISPRICE);
                String event_startday = item.getString(TAG_STARTDAY);
                String event_endday = item.getString(TAG_ENDDAY);

                // 전체 항목에는 모두 저장
                category_All.add(new user_Event_Item(event_number, event_name, event_URI,
                        event_price, event_dis_price, event_startday, event_endday));

                // 카테고리 분류해서 각각 저장
                if (event_type == 0) {
                    category_Culture.add(new user_Event_Item(event_number, event_name, event_URI,
                            event_price, event_dis_price, event_startday, event_endday));
                } else if (event_type == 1) {
                    category_Meal.add(new user_Event_Item(event_number, event_name, event_URI,
                            event_price, event_dis_price, event_startday, event_endday));
                } else if (event_type == 2) {
                    category_Beauty.add(new user_Event_Item(event_number, event_name, event_URI,
                            event_price, event_dis_price, event_startday, event_endday));
                } else if (event_type == 3) {
                    category_Fashion.add(new user_Event_Item(event_number, event_name, event_URI,
                            event_price, event_dis_price, event_startday, event_endday));
                }
            }

            rAdapter1 = new user_Event_Adapter(category_All, mContext);
            rAdapter2 = new user_Event_Adapter(category_Culture, mContext);
            rAdapter3 = new user_Event_Adapter(category_Meal, mContext);
            rAdapter4 = new user_Event_Adapter(category_Beauty, mContext);
            rAdapter5 = new user_Event_Adapter(category_Fashion, mContext);

            binding_UserMain.rviewContent1.setAdapter(rAdapter1);
            binding_UserMain.rviewContent2.setAdapter(rAdapter2);
            binding_UserMain.rviewContent3.setAdapter(rAdapter3);
            binding_UserMain.rviewContent4.setAdapter(rAdapter4);
            binding_UserMain.rviewContent5.setAdapter(rAdapter5);

            rAdapter1.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.d(TAG, "addItemInCategory Error : ", e);
        }
    } // JSON 데이터를 카테고리에 저장

    @Override
    protected void onDestroy() {
        if (getEventDB != null) {
            getEventDB.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (getEventDB != null) {
            getEventDB.cancel(true);
        }
        super.onPause();
    }

}

package com.example.win.a2vent.user.main;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.win.a2vent.user.entry_list.Activity_User_Entry_List;
import com.example.win.a2vent.user.map.Activity_User_Map;
import com.example.win.a2vent.user.account.Activity_User_Login;
import com.example.win.a2vent.util.GetImageURI;
import com.example.win.a2vent.util.GlobalData;
import com.example.win.a2vent.R;
import com.example.win.a2vent.databinding.ActivityUserEventMainBinding;
import com.example.win.a2vent.util.ServiceGPSInfo;

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
 * Android & PHP & MySQL 예제
 * http://webnautes.tistory.com/category/Android/DATABASE
 * 데이터 바인딩 라이브러리
 * https://developer.android.com/topic/libraries/data-binding/index.html?hl=ko
 */

public class Activity_User_Event_Main extends AppCompatActivity {
    private String TAG = "UserMain";

    private boolean flagRegisterURIReceiver = false;
    private boolean flagRegisterReceiver = false;
    private long backKeyPressedTime = 0;

    ActivityUserEventMainBinding binding_UserMain;
    Context mContext;
    RecyclerView.Adapter rAdapter1, rAdapter2, rAdapter3, rAdapter4, rAdapter5;
    ArrayList<User_Event_Item> category_All, category_Culture, category_Meal, category_Beauty, category_Fashion;
    GetEventDB getEventDB;
    GetImageURI getImageURI;
    String result; // GetEventDB의 result값 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity_User_Login.actList.add(this);
        binding_UserMain = DataBindingUtil.setContentView(this, R.layout.activity_user_event_main);
        set_TabHost(this);

        mContext = getApplicationContext();

        startGpsService(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!flagRegisterReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalData.USER_MAIN_RECEIVER);
            mContext.registerReceiver(broadcastReceiver, filter);
            flagRegisterReceiver = true;
        }
        if (!flagRegisterURIReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalData.GET_URI_RECEIVER);
            mContext.registerReceiver(broadcastURIReceiver, filter);
            flagRegisterURIReceiver = true;
        }
        getEventDB = new GetEventDB();
        getEventDB.execute(GlobalData.getURL() + "2ventGetEventAll.php"); // AsyncTask 실행
    }

    public void onClick_Accountinfo(View v) {
        Intent GoUserinfo = new Intent(this, Activity_User_Info.class);
        startActivity(GoUserinfo);
    } // 계정 정보

    public void onClick_Userentry(View v) {
        Intent GoUserentry = new Intent(this, Activity_User_Entry_List.class);
        startActivity(GoUserentry);
    } // 사용자가 참여한 이벤트 목록

    public void onClick_Help(View v) {
        Activity_User_Login.toast = Toast.makeText(mContext, "도움말 미구현", Toast.LENGTH_SHORT);
        Activity_User_Login.toast.show();
    } // 도움말 (미구현)

    public void onClick_goMap(View v) {
        Intent intent = new Intent(this, Activity_User_Map.class);
        startActivity(intent);
    } // 지도보기

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Activity_User_Login.toast = Toast.makeText(Activity_User_Event_Main.this,
                    "'뒤로' 버튼을 한번 더 누르면 로그아웃", Toast.LENGTH_SHORT);
            Activity_User_Login.toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            Activity_User_Login.toast.cancel();
            Intent intent_Logout = new Intent(Activity_User_Event_Main.this, Activity_User_Login.class);
            for (int i = 0; i < Activity_User_Login.actList.size(); i++)
                Activity_User_Login.actList.get(i).finish();
            startActivity(intent_Logout);
            finish(); // 액티비티 종료 (정확하게는 onDestroy() 호출)
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
                    isNoItemCateogry(category_All, binding_UserMain.rviewContent1, rAdapter1);
                    rAdapter1.notifyDataSetChanged();
                } else if (tabId == "문화") {
                    isNoItemCateogry(category_Culture, binding_UserMain.rviewContent2, rAdapter2);
                    rAdapter2.notifyDataSetChanged();
                } else if (tabId == "외식") {
                    isNoItemCateogry(category_Meal, binding_UserMain.rviewContent3, rAdapter3);
                    rAdapter3.notifyDataSetChanged();
                } else if (tabId == "뷰티") {
                    isNoItemCateogry(category_Beauty, binding_UserMain.rviewContent4, rAdapter4);
                    rAdapter4.notifyDataSetChanged();
                } else if (tabId == "패션") {
                    isNoItemCateogry(category_Fashion, binding_UserMain.rviewContent5, rAdapter5);
                    rAdapter5.notifyDataSetChanged();
                }
            }
        });
    } // TabWidget 설정

    private class GetEventDB extends AsyncTask<String, Void, String> {
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
                }
                ;
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
                Activity_User_Event_Main.this.result = result;
                getImageURI = new GetImageURI(mContext);
                getImageURI.execute("0", "0", "0", "");
            }
        }

    } // EventDB 받는 AsyncTask

    private BroadcastReceiver broadcastURIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String value = intent.getExtras().getString("finish");
            addItemInCategory(result, value);
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");
            ServiceGPSInfo.setPermission(Activity_User_Event_Main.this, Activity_User_Event_Main.this);
            if (!ServiceGPSInfo.isGetLocation()) {
                ServiceGPSInfo.showSettingsAlert(Activity_User_Event_Main.this);
            }
        }
    };

    private void addItemInCategory(String result, String value) {
        category_All = new ArrayList<>();
        category_Culture = new ArrayList<>();
        category_Meal = new ArrayList<>();
        category_Beauty = new ArrayList<>();
        category_Fashion = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Event");
            JSONObject jsonObject2 = new JSONObject(value);
            JSONArray jsonArray2 = jsonObject2.getJSONArray("EventMainImage");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                JSONObject item2 = jsonArray2.getJSONObject(i);

                int event_number = item.getInt("event_number");
                String event_name = item.getString("event_name");
                String event_content = item.getString("event_content");
                int com_category = item.getInt("com_category");
                String event_price = item.getString("event_price");
                String event_dis_price = item.getString("event_dis_price");
                String event_startday = item.getString("event_startday");
                String event_endday = item.getString("event_endday");
                String event_URI = item2.getString("event_URI");

                // 전체 항목에는 모두 저장
                category_All.add(new User_Event_Item(event_number, event_name, event_content,
                        event_price, event_dis_price, event_startday, event_endday, event_URI));

                // 카테고리 분류해서 각각 저장
                if (com_category == 0) {
                    category_Culture.add(new User_Event_Item(event_number, event_name, event_content,
                            event_price, event_dis_price, event_startday, event_endday, event_URI));
                } else if (com_category == 1) {
                    category_Meal.add(new User_Event_Item(event_number, event_name, event_content,
                            event_price, event_dis_price, event_startday, event_endday, event_URI));
                } else if (com_category == 2) {
                    category_Beauty.add(new User_Event_Item(event_number, event_name, event_content,
                            event_price, event_dis_price, event_startday, event_endday, event_URI));
                } else if (com_category == 3) {
                    category_Fashion.add(new User_Event_Item(event_number, event_name, event_content,
                            event_price, event_dis_price, event_startday, event_endday, event_URI));
                }
            }

            rAdapter1 = new User_Event_Adapter(category_All, mContext);
            rAdapter2 = new User_Event_Adapter(category_Culture, mContext);
            rAdapter3 = new User_Event_Adapter(category_Meal, mContext);
            rAdapter4 = new User_Event_Adapter(category_Beauty, mContext);
            rAdapter5 = new User_Event_Adapter(category_Fashion, mContext);

            isNoItemCateogry(category_All, binding_UserMain.rviewContent1, rAdapter1);
            isNoItemCateogry(category_Culture, binding_UserMain.rviewContent2, rAdapter2);
            isNoItemCateogry(category_Meal, binding_UserMain.rviewContent3, rAdapter3);
            isNoItemCateogry(category_Beauty, binding_UserMain.rviewContent4, rAdapter4);
            isNoItemCateogry(category_Fashion, binding_UserMain.rviewContent5, rAdapter5);

        } catch (JSONException e) {
            Log.d(TAG, "addItemInCategory Error : ", e);
        }
    } // JSON 데이터를 카테고리에 저장

    public void isNoItemCateogry(ArrayList<User_Event_Item> Arrlist,
                                 RecyclerView rView, RecyclerView.Adapter rAdapter) {
        if (Arrlist.size() > 0) {
            rView.setAdapter(rAdapter);
        } else {
            rView.setBackgroundResource(R.drawable.no_event);
        }
    }
    // 아이템(이벤트)이 없는 카테고리면 이벤트가 없다는 이미지를 표시하도록

    private boolean isGpsServiceRunning(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startGpsService(Context context) {
        if (!isGpsServiceRunning(context, GlobalData.USER_MAP_SERVICE)) {
            context.startService(new Intent(GlobalData.USER_MAP_SERVICE).setPackage("com.example.win.a2vent"));
            Log.d("테스트", "startGpsService");
        }
    }

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
        if (flagRegisterURIReceiver) {
            mContext.unregisterReceiver(broadcastURIReceiver);
            flagRegisterURIReceiver = false;
        }
        if (flagRegisterReceiver) {
            mContext.unregisterReceiver(broadcastReceiver);
            flagRegisterReceiver = false;
        }
        super.onPause();
    }

}

package com.example.win.a2vent;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.win.a2vent.databinding.ActivityOwnerEventMainBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import static com.example.win.a2vent.Activity_User_Login.actList;

/**
 * Created by win on 2017-07-06.
 */

public class Activity_Owner_Event_Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "테스트";
    private int nType = 1;

    private Context mContext;
    private FloatingActionButton fab;

    private GetCompanyTask GetCompanyTask;
    private GetEventTask getEventTask;
    private ActivityOwnerEventMainBinding binding_OwnerMain;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private ArrayList arrListCompany, arrListEvent;

    private int flagNvigation = -1;
    private boolean flagRegisterReceiver = false;

    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
        binding_OwnerMain = DataBindingUtil.setContentView(this, R.layout.activity_owner_event_main);

        mContext = getApplicationContext();
        mRecyclerView = binding_OwnerMain.eventOwnerMain;

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(Activity_Owner_Event_Main.this, "Permission Denied\n"
                        + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this).setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA).check();

        Toolbar toolbar = (Toolbar) findViewById(R.id.owner_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nType == 1) {
                    Intent intent_eventform = new Intent(getBaseContext(), Activity_Owner_Add_Event.class);
                    startActivity(intent_eventform);
                } else if (nType == 0) {
                    Intent intent_addstore = new Intent(getBaseContext(), Activity_Owner_Add_Store.class);
                    startActivity(intent_addstore);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nType = 1;
                if (getEventTask != null) {
                    getEventTask = null;
                }
                getEventTask = new GetEventTask();
                getEventTask.execute("0", GlobalData.getUserID());
            }
        }, 100);

    }

    public void onClick_owner_Accountinfo(View v) {
        Intent GoOwnerinfo = new Intent(this, Activity_Owner_Info.class);
        startActivity(GoOwnerinfo);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(Activity_Owner_Event_Main.this,
                    "'뒤로' 버튼을 한번 더 누르면 로그아웃", Toast.LENGTH_SHORT).show();
            return;
        } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            Intent intent_Logout = new Intent(Activity_Owner_Event_Main.this, Activity_User_Login.class);
            for(int i=0; i<actList.size(); i++)
                actList.get(i).finish();
            startActivity(intent_Logout);
            finish(); // 액티비티 종료 (정확하게는 onDestroy() 호출)
        }
    } // 백키 2번 로그아웃

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d(TAG, "id: " + id);

        if (id == R.id.my_company) {
            nType = 0;
            flagNvigation = 0;
            mRecyclerView.removeOnItemTouchListener(null);
            GetCompanyTask = new GetCompanyTask();
            GetCompanyTask.execute();
            Log.d(TAG, "내 업소");
        } else if (id == R.id.on_event) {
            nType = 1;
            flagNvigation = 1;
            if (getEventTask != null) {
                getEventTask = null;
            }
            getEventTask = new GetEventTask();
            getEventTask.execute("0", GlobalData.getUserID());
            Log.d(TAG, "진행중 이벤트");
        } else if (id == R.id.temp_event) {
            nType = 1;
            flagNvigation = 2;
            if (getEventTask != null) {
                getEventTask = null;
            }
            getEventTask = new GetEventTask();
            getEventTask.execute("1", GlobalData.getUserID());
            Log.d(TAG, "임시저장 이벤트");
        } else if (id == R.id.end_event) {
            nType = 1;
            flagNvigation = 3;
            if (getEventTask != null) {
                getEventTask = null;
            }
            getEventTask = new GetEventTask();
            getEventTask.execute("2", GlobalData.getUserID());
            Log.d(TAG, "종료된 이벤트");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!flagRegisterReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.example.win.a2vent.owner_Event_Main_Receiver");
            mContext.registerReceiver(broadcastReceiver, filter);
            flagRegisterReceiver = true;

            Log.d(TAG, "register receiver");
        }
        resume();
    }

    private void resume() {
        switch (flagNvigation) {
            case 0:
                break;
            case 1:
                if (getEventTask != null) {
                    getEventTask = null;
                }
                getEventTask = new GetEventTask();
                getEventTask.execute("0", GlobalData.getUserID());
                break;
            case 2:
                if (getEventTask != null) {
                    getEventTask = null;
                }
                getEventTask = new GetEventTask();
                getEventTask.execute("1", GlobalData.getUserID());
                break;
            case 3:
                if (getEventTask != null) {
                    getEventTask = null;
                }
                getEventTask = new GetEventTask();
                getEventTask.execute("2", GlobalData.getUserID());
                break;
        }
    }

    public class GetCompanyTask extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String phpPage = "2ventGetCompany.php";

            try {
                ServerConnector serverConnector = new ServerConnector(phpPage);

                serverConnector.addPostData("id", GlobalData.getUserID());
                serverConnector.addDelimiter();

                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d(TAG, "GetCompanyTask Error : ", e);
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

        private void addItemInCategory(String result) {
            arrListCompany = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("Company");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    String com_number = item.getString("com_number");
                    String com_name = item.getString("com_name");
                    String com_addr = item.getString("com_addr");
                    String com_category = item.getString("com_category");
                    String com_manager = item.getString("com_manager");
                    String com_URI = item.getString("com_URI");
                    String id = item.getString("id");

                    if (GlobalData.getUserID().equals(id)) { // equals 객체의 내용 자체를 비교하지만 == 연산자는 대상의 주소값을 비교한다
                        if (com_category.equals("0")) {
                            arrListCompany.add(new Owner_Store_Item(com_number, com_name, com_addr,
                                    com_manager, "문화", com_URI, id));
                        } else if (com_category.equals("1")) {
                            arrListCompany.add(new Owner_Store_Item(com_number, com_name, com_addr,
                                    com_manager, "외식", com_URI, id));
                        } else if (com_category.equals("2")) {
                            arrListCompany.add(new Owner_Store_Item(com_number, com_name, com_addr,
                                    com_manager, "뷰티", com_URI, id));
                        } else if (com_category.equals("3")) {
                            arrListCompany.add(new Owner_Store_Item(com_number, com_name, com_addr,
                                    com_manager, "패션", com_URI, id));
                        }
                    }

                }

                mRecyclerViewAdapter = new Owner_Store_Adapter(arrListCompany, Activity_Owner_Event_Main.this);

                mRecyclerView.setAdapter(mRecyclerViewAdapter);

                mRecyclerViewAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d(TAG, "addItemInCategory Error : ", e);
            }
        } // JSON 데이터를 카테고리에 저장
    } // EventDB 받는 AsyncTask

    private class GetEventTask extends AsyncTask<String, Void, String> {
        String event_stats;
        String id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            event_stats = params[0];
            id = params[1];

            try {
                ServerConnector serverConnector = new ServerConnector("2ventGetStoreEvent.php");

                serverConnector.addPostData("event_stats", event_stats);
                serverConnector.addPostData("id", id);
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
            Log.d(TAG, "response  - " + result);

            if (result == null) {

            } else {
                addItem(result);
            }
        }

        private void addItem(String result) {
            arrListEvent = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("EventList");

                JSONObject item;
                String event_number;
                String event_name;
                String event_type;
                String event_URI;
                String event_price;
                String event_dis_price;
                String event_startday;
                String event_endday;
                String event_starttime;
                String event_endtime;
                String com_name;

                String tmp_year, tmp_month, tmp_day, tmp_hour, tmp_min;
                DecimalFormat format = new DecimalFormat("###,###");

                for (int i = 0; i < jsonArray.length(); i++) {
                    item = jsonArray.getJSONObject(i);

                    event_number = item.getString("event_number");
                    event_name = item.getString("event_name");
                    event_type = item.getString("event_type");
                    event_URI = item.getString("event_URI");
                    event_price = item.getString("event_price");
                    event_dis_price = item.getString("event_dis_price");
                    event_startday = item.getString("event_startday");
                    event_endday = item.getString("event_endday");
                    event_starttime = item.getString("event_starttime");
                    event_endtime = item.getString("event_endtime");
                    com_name = item.getString("com_name");

                    if (event_type.equals("0")) {
                        event_type = "응모형";
                    } else if (event_type.equals("1")) {
                        event_type = "결제형";
                    }

                    if (event_price.length() != 0) {
                        event_price = format.format(Long.parseLong(event_price)) + "원";
                    }

                    if (event_dis_price.length() != 0) {
                        event_dis_price = format.format(Long.parseLong(event_dis_price)) + "원";
                    }

                    StringTokenizer tokenDay = new StringTokenizer(event_startday, "-");
                    StringTokenizer tokenTime = new StringTokenizer(event_starttime, ":");
                    try {
                        tmp_year = tokenDay.nextToken();
                        tmp_month = tokenDay.nextToken();
                        tmp_day = tokenDay.nextToken();
                        tmp_hour = tokenTime.nextToken();
                        tmp_min = tokenTime.nextToken();

                        event_startday = tmp_year + "년 " + tmp_month + "월 " + tmp_day + "일\n"
                                + tmp_hour + "시 " + tmp_min + "분";
                    } catch (NoSuchElementException e) {

                    }

                    tokenDay = new StringTokenizer(event_endday, "-");
                    tokenTime = new StringTokenizer(event_endtime, ":");
                    try {
                        tmp_year = tokenDay.nextToken();
                        tmp_month = tokenDay.nextToken();
                        tmp_day = tokenDay.nextToken();
                        tmp_hour = tokenTime.nextToken();
                        tmp_min = tokenTime.nextToken();

                        event_endday = tmp_year + "년 " + tmp_month + "월 " + tmp_day + "일\n"
                                + tmp_hour + "시 " + tmp_min + "분";
                    } catch (NoSuchElementException e) {

                    }

                    arrListEvent.add(new Owner_Event_Simple_Item(event_number, event_name, event_type, event_URI, event_price, event_dis_price,
                            event_startday, event_endday, com_name));
                }

                mRecyclerViewAdapter = new Owner_Event_Simple_Adapter(arrListEvent, Activity_Owner_Event_Main.this, Integer.parseInt(event_stats));

                mRecyclerView.setAdapter(mRecyclerViewAdapter);

                mRecyclerViewAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (GetCompanyTask != null) {
            GetCompanyTask.cancel(true);
        }

        if (getEventTask != null) {
            getEventTask.cancel(true);
        }

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (GetCompanyTask != null) {
            GetCompanyTask.cancel(true);
        }

        if (getEventTask != null) {
            getEventTask.cancel(true);
        }

        super.onPause();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String value = intent.getExtras().getString("delete_finish");

            if (value.equals("event_success")) {
                Toast.makeText(Activity_Owner_Event_Main.this, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show();
                resume();
            } else if (value.equals("store_success")) {
                Toast.makeText(Activity_Owner_Event_Main.this, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show();
                resume();
            }  else if (value.equals("store_failure")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Owner_Event_Main.this);
                builder.setMessage("삭제를 완료할 수 없습니다.\n해당 매장에서 등록 된 이벤트가 존재합니다.").setCancelable(false)
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    };
}

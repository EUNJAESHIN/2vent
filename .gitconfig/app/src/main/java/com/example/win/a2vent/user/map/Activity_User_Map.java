package com.example.win.a2vent.user.map;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.win.a2vent.user.account.Activity_User_Login;
import com.example.win.a2vent.user.details_info.Activity_User_Event_Details_Info;
import com.example.win.a2vent.user.main.Activity_User_Event_Main;
import com.example.win.a2vent.util.GlobalData;
import com.example.win.a2vent.R;
import com.example.win.a2vent.util.ServerConnector;
import com.example.win.a2vent.util.ServiceGPSInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017-07-03.
 */

public class Activity_User_Map extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    private final String TAG = "테스트";

    private MapView mMapView;

    private FloatingActionButton mFabMap;

    private LinearLayout mLayoutSimpleInfo;

    private ArrayList<SimpleInfoListItem> simpleInfoItemList;
    private ArrayList<SimpleInfoListItem> tempSimpleInfoItemList;

    private boolean flagRegisterReceiver = false;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private int mRecyclerViewIndex = 0;
    private int mRecyclerViewCount = 0;

    private TextView tvSimpleInfoEventName;
    private TextView tvSimpleInfoPrice;
    private TextView tvSimpleInfoDisPrice;
    private TextView tvSimpleInfoEndDate;
    private ImageView ivSimpleInfoImage;
    private Button btnSimpleInfoLeft;
    private Button btnSimpleInfoRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity_User_Login.actList.add(this);
        setContentView(R.layout.activity_user_map);

        // 맵뷰 생성 및 뷰컨테이너에 추가
        MapLayout mapLayout = new MapLayout(this);
        mMapView = mapLayout.getMapView();

        mMapView.setMapViewEventListener(this);
        mMapView.setDrawingCacheEnabled(true);

        ViewGroup mapViewContainer = (RelativeLayout) findViewById(R.id.mapView);
        mapViewContainer.addView(mapLayout);

        mFabMap = (FloatingActionButton) findViewById(R.id.fabMap);
        mFabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_goMain = new Intent(Activity_User_Map.this, Activity_User_Event_Main.class);
                startActivity(intent_goMain);
            }
        });
        mFabMap.bringToFront();

        tvSimpleInfoEventName = (TextView) findViewById(R.id.simple_info_event_name);
        tvSimpleInfoPrice = (TextView) findViewById(R.id.simple_info_price);
        tvSimpleInfoDisPrice = (TextView) findViewById(R.id.simple_info_discount);
        tvSimpleInfoEndDate = (TextView) findViewById(R.id.simple_info_end_date);
        ivSimpleInfoImage = (ImageView) findViewById(R.id.simple_info_image);
        btnSimpleInfoLeft = (Button) findViewById(R.id.btn_simple_info_left);
        btnSimpleInfoLeft.setOnClickListener(new OnClickListener());
        btnSimpleInfoRight = (Button) findViewById(R.id.btn_simple_info_right);
        btnSimpleInfoRight.setOnClickListener(new OnClickListener());

        mLayoutSimpleInfo = (LinearLayout) findViewById(R.id.rlSimpleInfo);
        mLayoutSimpleInfo.setOnClickListener(new OnClickListener());
        mLayoutSimpleInfo.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvEventCount);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!flagRegisterReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalData.USER_MAP_RECEIVER);
            registerReceiver(broadcastReceiver, filter);
            flagRegisterReceiver = true;
        }
        //new GetMapData().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {


        if (flagRegisterReceiver) {
            unregisterReceiver(broadcastReceiver);
            flagRegisterReceiver = false;
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish();
    }

    private void makeMarker(MapView mapView, double latitude, double longitude, String comName, int eventNumber) {
        MapPoint defaultMarkerPoint;
        MapPOIItem defaultMarker = new MapPOIItem();

        if (ServiceGPSInfo.checkDistance(latitude, longitude) < 1000) {
            defaultMarkerPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

            defaultMarker.setItemName(comName);
            defaultMarker.setTag(eventNumber);
            defaultMarker.setMapPoint(defaultMarkerPoint);
        }

        mapView.addPOIItem(defaultMarker);
        mapView.invalidate();
    }

    private void setFadeInUpAnimation(LinearLayout layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_in_up);
            layout.setAnimation(animation);
            layout.bringToFront();
        }
    }

    private void setFadeOutDownAnimation(MapView mapView, FloatingActionButton fab, LinearLayout layout) {
        if (layout.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_out_down);
            layout.setAnimation(animation);
            layout.setVisibility(View.GONE);
            mapView.bringToFront();
            fab.bringToFront();
        }
    }

    private ArrayList<SimpleInfoListItem> countEvent(MapPOIItem mapPOIItem, int index) {
        int count = 0;
        ArrayList<SimpleInfoListItem> tempEventNumberList = new ArrayList<>();

        for (int i = 0; i < simpleInfoItemList.size(); i++) {
            if (mapPOIItem.getItemName().equals(simpleInfoItemList.get(i).getComName())) {
                tempEventNumberList.add(simpleInfoItemList.get(i));
                count++;
            }
        }

        mRecyclerViewCount = count;
        createRecyclerView(mRecyclerViewCount, index);

        return tempEventNumberList;
    }

    private void createRecyclerView(int count, int index) {
        mRecyclerViewAdapter = new SimpleInfoAdapter(count, index);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void slideViewUpdate(ArrayList<SimpleInfoListItem> list, int index) {
        Picasso.with(getApplicationContext()).load(GlobalData.getURL() + list.get(index).getEventURI())
                .placeholder(R.drawable.event_default).into(ivSimpleInfoImage);
        tvSimpleInfoEventName.setText(list.get(index).getEventName());
        tvSimpleInfoPrice.setText(list.get(index).getEventPrice());
        tvSimpleInfoDisPrice.setText(list.get(index).getEventDisPrice());
        tvSimpleInfoEndDate.setText(list.get(index).getEventEndDate());

    }

    private class GetMapData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Activity_User_Map.this,
                    "wait", null, true, false);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ServerConnector serverConnector = new ServerConnector("2ventGetEventOnMap.php");

                serverConnector.addDelimiter();

                serverConnector.writePostData();

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
                parseJson(result);
            }

            progressDialog.dismiss();
        }

        private void parseJson(String result) {

            try {
                simpleInfoItemList = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("eventInfo");
                JSONObject item;

                int event_number;
                String event_name, event_price, event_dis_price, event_endday, event_endtime,
                        event_URI, com_name, com_addr;

                Log.d(TAG, "json 크기 : " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    item = jsonArray.getJSONObject(i);
                    event_number = item.getInt("event_number");
                    event_name = item.getString("event_name");
                    event_price = item.getString("event_price");
                    event_dis_price = item.getString("event_dis_price");
                    event_endday = item.getString("event_endday");
                    event_endtime = item.getString("event_endtime");
                    event_URI = item.getString("event_URI");
                    com_name = item.getString("com_name");
                    com_addr = item.getString("com_addr");

                    Log.d(TAG, event_number + ". 가게이름: " + com_name + ", 주소: " + com_addr);
                    //makeMarker(mMapView, event_number, com_name, com_addr);

                    simpleInfoItemList.add(new SimpleInfoListItem(event_number, event_name, event_price,
                            event_dis_price, event_endday, event_endtime, event_URI, com_name, com_addr));

                    new KakaoLocaRESTapi().execute(com_addr, com_name, String.valueOf(event_number));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class KakaoLocaRESTapi extends AsyncTask<String, Void, String> {

        String comName;
        int eventNumber;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            comName = params[1];
            eventNumber = Integer.parseInt(params[2]);
            try {
                String requestQuery = "https://dapi.kakao.com/v2/local/search/address.json?query=".concat(URLEncoder.encode(params[0], "UTF8"));
                URL url = new URL(requestQuery);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Connection", "Keep_Alive");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                httpURLConnection.setRequestProperty("Authorization", "KakaoAK " + "87b25dce3d9299f88e89a88aea8822c0");
                httpURLConnection.setRequestProperty("Cache-Control", "no-cache");

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;

                if (responseStatusCode == httpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "EUC-KR");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer sb = new StringBuffer();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString();

            } catch (MalformedURLException e) {
                return new String("MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                return new String("IOException: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("documents");
                JSONObject item;

                String x, y;

                item = jsonArray.getJSONObject(0);

                jsonObject = new JSONObject(item.getString("road_address"));

                x = jsonObject.getString("x");
                y = jsonObject.getString("y");

                Log.d(TAG, "x: " + x + ", y: " + y);

                makeMarker(mMapView, Double.parseDouble(y), Double.parseDouble(x), comName, eventNumber);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_simple_info_left:
                    if (--mRecyclerViewIndex < 0) {
                        mRecyclerViewIndex = mRecyclerViewCount - 1;
                    }
                    createRecyclerView(mRecyclerViewCount, mRecyclerViewIndex);
                    slideViewUpdate(tempSimpleInfoItemList, mRecyclerViewIndex);
                    break;
                case R.id.btn_simple_info_right:
                    if (++mRecyclerViewIndex == mRecyclerViewCount) {
                        mRecyclerViewIndex = 0;
                    }
                    createRecyclerView(mRecyclerViewCount, mRecyclerViewIndex);
                    slideViewUpdate(tempSimpleInfoItemList, mRecyclerViewIndex);
                    break;
                case R.id.rlSimpleInfo:
                    if (mLayoutSimpleInfo.getVisibility() == View.VISIBLE) {
                        Intent intent = new Intent(Activity_User_Map.this, Activity_User_Event_Details_Info.class);
                        intent.putExtra("event_number", tempSimpleInfoItemList.get(mRecyclerViewIndex).getEventNumber());
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    //======================================================================================================================
    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.d(TAG, "onMapViewInitialized");

        ServiceGPSInfo.getLocation();

        {
            mapView.setCurrentLocationTrackingMode
                    (MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(ServiceGPSInfo.getLatitude(), ServiceGPSInfo.getLongitude()), false);
            mapView.setZoomLevel(3, true);
            mapView.setCurrentLocationRadius(500);
            mapView.setCurrentLocationRadiusFillColor(Color.argb(20, 255, 255, 255));

            mapView.setPOIItemEventListener(this);
            mapView.setCurrentLocationEventListener(this);
            mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
            //setCurrentLocationMarker(mapView);

            mapView.invalidate();
            //onCurrentLocationUpdate(mapView, MapPoint.mapPointWithGeoCoord(36,127), 15);

        }

        Intent intent = new Intent(GlobalData.USER_MAP_RECEIVER);
        intent.putExtra("MAP", "init");
        sendBroadcast(intent);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "onMapViewCenterPointMoved");
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        Log.d(TAG, "onMapViewZoomLevelChanged");
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "onMapViewSingleTapped");
        setFadeOutDownAnimation(mMapView, mFabMap, mLayoutSimpleInfo);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "onMapViewDoubleTapped");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "onMapViewLongPressed");
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "onMapViewDragStarted");
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "onMapViewDragEnded");
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "onMapViewMoveFinished");
        while (ServiceGPSInfo.getLatitude() == 0 && ServiceGPSInfo.getLongitude() == 0) {
            try {
                ServiceGPSInfo.getLocation();
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
            mapView.setCurrentLocationTrackingMode
                    (MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(ServiceGPSInfo.getLatitude(), ServiceGPSInfo.getLongitude()), false);
        }
    }

    //============================================================================================

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        mRecyclerViewIndex = 0;
        tempSimpleInfoItemList = countEvent(mapPOIItem, mRecyclerViewIndex);
        slideViewUpdate(tempSimpleInfoItemList, mRecyclerViewIndex);
        setFadeInUpAnimation(mLayoutSimpleInfo);
        mapPOIItem.setMoveToCenterOnSelect(true);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    //=================================================================================================

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        Log.d(TAG, "onCurrentLocationUpdate: " + v);

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
        Log.d(TAG, "onCurrentLocationDeviceHeadingUpdate");
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.d(TAG, "onCurrentLocationUpdateFiled");
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.d(TAG, "onCurrentLocationUpdateCancelled");
    }

    //===============================================================================================

    private class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem mapPOIItem) {
            //((ImageView) mCalloutBalloon.findViewById(R.id.badge)).setImageBitmap("");
            return null;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
            return null;
        }
    }

    //===============================================================================================

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getExtras().getString("MAP");

            if (value.equals("geocoder")) {

            } else if (value.equals("init")) {
                new GetMapData().execute();
                //new KakaoLocaRESTapi().execute("대구광역시 동구 신천동 286-2");
            }
        }
    };
}
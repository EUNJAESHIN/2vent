package com.example.win.a2vent;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.win.a2vent.databinding.ActivityUserMapBinding;
import com.example.win.a2vent.ServiceGPSInfo.ServiceGPSInfoBinder;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017-07-03.
 */

public class Activity_User_Map extends AppCompatActivity {

    private final String TAG = "테스트";

    private MapView mMapView;

    private FloatingActionButton mFabMap;

    private RelativeLayout mLayoutSimpleInfo;

    private ServiceGPSInfo serviceGPSInfo;

    private static boolean isBindService = false;
    private boolean flagRegisterGPSRecevier = false;

    private int selectedNumberInMaker = 0;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceGPSInfoBinder binder = (ServiceGPSInfoBinder) service;
            serviceGPSInfo = binder.getService(Activity_User_Map.this);

            isBindService = true;

            serviceGPSInfo.startThread();
            //initMapView();

            Log.d(TAG, "onServiceConnected, isBindService : " + isBindService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        // 권한 부여
        setPermission();

        // 맵뷰 생성 및 뷰컨테이너에 추가
        mMapView = new MapView(this);

        ViewGroup mapViewContainer = (RelativeLayout) findViewById(R.id.mapView);

        MapViewEventListener mapViewEventListener = new MapViewEventListener();
        POIItemEventListener poiItemEventListener = new POIItemEventListener();
        CurrentLocationEventListener currentLocationEventListener = new CurrentLocationEventListener();
        CustomCalloutBalloonAdapter customCalloutBalloonAdapter = new CustomCalloutBalloonAdapter();

        mMapView.setMapViewEventListener(mapViewEventListener);
        mMapView.setPOIItemEventListener(poiItemEventListener);
        mMapView.setCurrentLocationEventListener(currentLocationEventListener);
        mMapView.setCalloutBalloonAdapter(customCalloutBalloonAdapter);

        mapViewContainer.addView(mMapView);

        mFabMap =(FloatingActionButton) findViewById(R.id.fabMap);
        mFabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mFabMap.bringToFront();

        mLayoutSimpleInfo = (RelativeLayout) findViewById(R.id.rlSimpleInfo);
        mLayoutSimpleInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mLayoutSimpleInfo.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isBindService) {
            Log.d(TAG, "onResume - bind, isBindService : " + isBindService);
            Intent intent = new Intent(Activity_User_Map.this, ServiceGPSInfo.class);

            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

            //new GetData().execute();
        }

        if (!flagRegisterGPSRecevier) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalData.USER_MAP_RECEVIER);
            registerReceiver(broadcastGPSReceiver, filter);
            flagRegisterGPSRecevier = true;
        }
    }

    @Override
    protected void onPause() {

        if (flagRegisterGPSRecevier) {
            unregisterReceiver(broadcastGPSReceiver);
            flagRegisterGPSRecevier = false;
        }

        try {
            if (isBindService) {
                Log.d(TAG, "onPause - unbind, isBindService : " + isBindService);
                serviceGPSInfo.stopUsingGPS();
                isBindService = false;
                unbindService(serviceConnection);
            }
        } catch (IllegalArgumentException e) {
            isBindService = false;
        }

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish();
    }

    private void setPermission() {
        //권한 부여
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(Activity_User_Map.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this).setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE).check();
    }

    private void initMapView() {
        Log.d(TAG, "initMapView");
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
        try {
            mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(serviceGPSInfo.getLatitude(), serviceGPSInfo.getLongitude()), 2, true);
        } catch (NullPointerException e) {
            Log.d(TAG, "initMapView - NullPointerException");
            initMapView();

        }
    }

    private class MapViewEventListener implements MapView.MapViewEventListener {

        @Override
        public void onMapViewInitialized(MapView mapView) {
            new GetData().execute();
        }

        @Override
        public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewZoomLevelChanged(MapView mapView, int i) {

        }

        @Override
        public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

        }
    }

    private class POIItemEventListener implements MapView.POIItemEventListener {

        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

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
    }

    private class CurrentLocationEventListener implements MapView.CurrentLocationEventListener {

        @Override
        public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

        }

        @Override
        public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

        }

        @Override
        public void onCurrentLocationUpdateFailed(MapView mapView) {

        }

        @Override
        public void onCurrentLocationUpdateCancelled(MapView mapView) {

        }
    }

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

    private class GetData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
        }

        private void parseJson(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("eventInfo");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    String event_number = item.getString("event_number");
                    String com_name = item.getString("com_name");
                    String com_addr = item.getString("com_addr");

                    Log.d(TAG, event_number + ". 가게이름: " + com_name + ", 주소: " + com_addr);
                    makeMarker(mMapView, event_number, com_name, com_addr);
                }
                mMapView.fitMapViewAreaToShowAllPOIItems();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeMarker(MapView mapView, String event_number, String com_name, String com_addr) {
        double latitude, longitude;
        Geocoder geocoder = new Geocoder(Activity_User_Map.this);
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(com_addr, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        latitude = list.get(0).getLatitude();
        longitude = list.get(0).getLongitude();

        MapPoint defaultMarkerPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        MapPOIItem defaultMarker = new MapPOIItem();

        defaultMarker.setItemName(com_name);
        defaultMarker.setTag(1);
        defaultMarker.setMapPoint(defaultMarkerPoint);
        defaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        defaultMarker.setCustomImageResourceId(R.drawable.map_pin);
        defaultMarker.setCustomImageAutoscale(true);
        defaultMarker.setCustomImageAnchor(0.5f, 1.0f);
        defaultMarker.setUserObject(event_number);

        Log.d(TAG, "거리 : " + String.valueOf(checkDistance(latitude, longitude)));
        if (checkDistance(latitude, longitude) < 2000) {
            mapView.addPOIItem(defaultMarker);
        }

    }

    private double checkDistance(double latitude, double longitude) {
        LatLng to = new LatLng(latitude, longitude);
        LatLng from = new LatLng(latitude, longitude);

        return SphericalUtil.computeDistanceBetween(to, from);
    }

    private BroadcastReceiver broadcastGPSReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getExtras().getString("GPS");

            if (value.equals("using")) {
                initMapView();
            } else if (value.equals("cancel")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Activity_User_Map.this.onBackPressed();
                    }
                }, 2000);
            } else if (value.equals("changed")) {
                Log.d(TAG, "GPS changed");
                //mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(serviceGPSInfo.getLatitude(), serviceGPSInfo.getLongitude()), true);
            }
        }
    };
}
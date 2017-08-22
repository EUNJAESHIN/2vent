package com.example.win.a2vent.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * Created by win on 2017-08-04.
 */

public class ServiceGPSInfo extends Service implements LocationListener {

    private final static String TAG = "테스트";

    private static Context mContext;

    // 현재 GPS 사용 유무
    private static boolean isGPSEnabled = false;

    // 네트워크 사용 유무
    private static boolean isNetworkEnabled = false;

    // GSP 상태 값
    private static boolean isGetLocation = false;

    private static Location mLocation;

    private static double mLatitude; // 위도
    private static double mLongitude; // 경도

    // 최소 GPS 정보 업데이트 거리 1M
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = (long) 0.01;

    // 최소 GPS 정보 업데이트 시간 3 sec
    private static final long MIN_TIME_BW_UPDATES = (long) (1000 * 0.1 * 1);

    protected static LocationManager mLocationManager;

    private static ServiceGPSInfo serviceGPSInfo;

    private boolean flagRegisterReceiver = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service- onCreate");
        mContext = getApplicationContext();
        serviceGPSInfo = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service - onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service - onStartCommand");
        getLocation();

        if (!flagRegisterReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalData.USER_GPS_RECEIVER);
            registerReceiver(broadcastReceiver, filter);
            flagRegisterReceiver = true;
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service - onDestroy");

        if (flagRegisterReceiver) {
            unregisterReceiver(broadcastReceiver);
            flagRegisterReceiver = false;
        }

        super.onDestroy();
    }

    public static double checkDistance(double toLatitude, double toLongitude) {
        LatLng to = new LatLng(toLatitude, toLongitude);
        LatLng from = new LatLng(mLatitude, mLongitude);
        return SphericalUtil.computeDistanceBetween(to, from);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        getLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static Location getLocation() {
        try {
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // GPS 정보 가져오기
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS와 네트워크 사용이 가능하지 않을 때

            } else {
                isGetLocation = true;

                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, serviceGPSInfo);

                    if (mLocationManager != null) {
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (mLocation != null) {
                            // 위도 경도 변수 저장
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (mLocation == null) {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, serviceGPSInfo);

                        if (mLocationManager != null) {
                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (mLocation != null) {
                                mLatitude = mLocation.getLatitude();
                                mLongitude = mLocation.getLongitude();
                            }
                        }
                    }
                }

                Log.d(TAG,"위도: " + getLatitue() + ", 경도: " + getLongitue());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return mLocation;
    }

    public void stopUsingGPS() {
        // GPS 종료
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(ServiceGPSInfo.this);
        }
    }

    public static double getLatitue() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }

        return mLatitude;
    }

    public static double getLongitue() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }

        return mLongitude;
    }

    public static boolean isGetLocation() {
        // GPS나 wifi 정보가 켜져있는지 확인
        return isGetLocation;
    }

    public static void showSettingsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("GPS Setting").setMessage("GPS 설정창으로 가시겠습니까?").setCancelable(false)
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getExtras().getString("GPS");

            if (value.equals("init")) {
                onLocationChanged(mLocation);
            }
        }
    };
}


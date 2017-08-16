package com.example.win.a2vent;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by win on 2017-08-04.
 */

public class ServiceGPSInfo extends Service implements LocationListener {

    private final String TAG = "테스트";

    private Context mContext;
    private Activity mActivity;

    private Thread threadCheckGPS;
    private boolean isInit = false;
    private boolean flagThreadCheckGPS = false;

    // 현재 GPS 사용 유무
    private boolean isGPSEnabled = false;

    // 네트워크 사용 유무
    private boolean isNetworkEnabled = false;

    // GSP 상태 값
    private boolean isGetLocation = false;

    private Location mLocation;

    private double mLatitude; // 위도
    private double mLongitude; // 경도

    // 최소 GPS 정보 업데이트 거리 1M
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    // 최소 GPS 정보 업데이트 시간 3 sec
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1;

    protected LocationManager mLocationManager;

    private IBinder mBinder = new ServiceGPSInfoBinder();

    public class ServiceGPSInfoBinder extends Binder {
        ServiceGPSInfo getService(Activity activity) {
            mActivity = activity;

            return ServiceGPSInfo.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service - onCreate");
        mContext = getApplicationContext();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service - onBind");
        getLocation();
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service - onDestroy");
        super.onDestroy();

        stopThread();
    }

    public void startThread() {
        if (!flagThreadCheckGPS) {
            threadCheckGPS = new Thread(runCheckGPS);
            threadCheckGPS.start();
            flagThreadCheckGPS = true;
        }
    }

    public void stopThread() {
        if (flagThreadCheckGPS) {
            threadCheckGPS.interrupt();
            threadCheckGPS = null;
            flagThreadCheckGPS = false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
        Log.d(TAG, "위도: " + getLatitude() + ", 경도: " + getLongitude());

        Intent intent = new Intent(GlobalData.USER_MAP_RECEVIER);
        intent.putExtra("GPS", "changed");
        mActivity.sendBroadcast(intent);
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

    public Location getLocation() {
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
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

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
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (mLocationManager != null) {
                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (mLocation != null) {
                                mLatitude = mLocation.getLatitude();
                                mLongitude = mLocation.getLongitude();
                            }
                        }
                    }
                }
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

    public double getLatitude() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }

        return mLatitude;
    }

    public double getLongitude() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }

        return mLongitude;
    }

    public boolean isGetLocation() {
        // GPS나 wifi 정보가 켜져있는지 확인
        return isGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("GPS Setting").setMessage("GPS 사용이 중지되어 있습니다.\n설정창으로 가시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mActivity.startActivity(intent);
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mActivity, "GPS를 사용하여야 지도 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GlobalData.USER_MAP_RECEVIER);
                intent.putExtra("GPS", "cancel");
                mActivity.sendBroadcast(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Runnable runCheckGPS = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }

                if (!isGetLocation()) {
                    isInit = false;
                    showSettingsAlert();
                } else if (!isInit && isGetLocation()) {
                    isInit = true;
                    Intent intent = new Intent(GlobalData.USER_MAP_RECEVIER);
                    intent.putExtra("GPS", "using");
                    mActivity.sendBroadcast(intent);
                }
            }
        }
    };
}

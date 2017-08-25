package com.example.win.a2vent.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by win on 2017-07-28.
 */

public class DeleteData extends AsyncTask<String, Void, String> {

    private final String TAG = "테스트";

    private String mKey;
    private String mColumnKey;
    private String mColumnValue;
    private Context mContext;

    public DeleteData(Context context, String key, String columnKey, String columnValue) {
        mKey = key;
        mColumnKey = columnKey;
        mColumnValue = columnValue;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String phpPage = params[0];

        try {
            Log.d(TAG, "서버 접속 중");
            ServerConnector serverConnector = new ServerConnector(phpPage);

            serverConnector.addPostData(mColumnKey, mColumnValue);
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
            Intent intent = new Intent(GlobalData.OWNER_MAIN_RECEIVER);
            intent.putExtra("result", Integer.parseInt(result.trim().toString()));
            intent.putExtra("keyValue", mKey);
            mContext.sendBroadcast(intent);
        } catch (NumberFormatException e) {

        }

        /*
        if (mKey.equals(EVENT_KEY)) {
            if (result.equals("SQL문 처리 성공")) {
                Log.d(TAG, "eventKey - 성공");
                intent.putExtra("delete_finish", "event_delete_success");
            } else {
                Log.d(TAG, "eventKey - 실패");
                intent.putExtra("delete_finish", "event_delete_failure");
            }
            mContext.sendBroadcast(intent);
        } else if (mKey.equals(STORE_KEY)) {
            if (result.equals("SQL문 처리 성공")) {
                intent.putExtra("delete_finish", "store_delete_success");
            } else {
                intent.putExtra("delete_finish", "store_delete_failure");
            }
            mContext.sendBroadcast(intent);
        }
        */
    }
}

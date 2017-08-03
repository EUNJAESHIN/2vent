package com.example.win.a2vent;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by win on 2017-07-28.
 */

public class DeleteData extends AsyncTask<String, Void, String> {

    private final String EVENT_KEY = "EventKey";
    private final String STORE_KEY = "StoreKey";

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
            Log.d("테스트", "서버 접속 중");
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
        Log.d("테스트", "response  - " + result);
        Intent intent = new Intent("com.example.win.a2vent.owner_Event_Main_Receiver");
        if (mKey.equals(EVENT_KEY)) {
            intent.putExtra("delete_finish", "event_success");
            mContext.sendBroadcast(intent);
        } else if (mKey.equals(STORE_KEY)) {
            String str1, str2;
            StringTokenizer token = new StringTokenizer(result, ":");
            try {
                str1 = token.nextToken();
                str2 = token.nextToken();

                if (str1.equals("SQL문 처리 성공")) {
                    intent.putExtra("delete_finish", "store_success");
                } else if (str1.equals("SQL문 처리중 에러 발생") && str2.equals("Cannot delete or update a parent row")) {
                    intent.putExtra("delete_finish", "store_failure");
                }
                mContext.sendBroadcast(intent);
            } catch (NoSuchElementException e) {}


        }
    }
}

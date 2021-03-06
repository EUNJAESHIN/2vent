package com.example.win.a2vent.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by EUNJAESHIN on 2017-08-07.
 */

public class GetImageURI extends AsyncTask<String, Void, String> {

    private Context mContext;

    public GetImageURI(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String serverURL = "2ventGetEventImage.php";

        try {
            ServerConnector serverConnector = new ServerConnector(serverURL);

            serverConnector.addPostData("event_number", params[0]);
            serverConnector.addPostData("event_stats", params[1]);
            serverConnector.addPostData("separator", params[2]);
            serverConnector.addPostData("com_number", params[3]);

            serverConnector.addDelimiter();
            serverConnector.writePostData();
            serverConnector.finish();

            return serverConnector.response();

        } catch (Exception e) {
            Log.d("DB", "GetImageURI Error ", e);

            return new String("Error: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("테스트", result);

        Intent intent = new Intent(GlobalData.GET_URI_RECEIVER);
        intent.putExtra("finish", result);
        mContext.sendBroadcast(intent);
    }
}

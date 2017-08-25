package com.example.win.a2vent.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.win.a2vent.onwer.add_event.Activity_Owner_Add_Event;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by win on 2017-08-24.
 */

public class ModifyData extends AsyncTask<String, Void, String> {

    private final String TAG = "테스트";

    private Context mContext;
    private String mEventNumber;

    public ModifyData(Context context, String eventNumber) {
        mContext = context;
        mEventNumber = eventNumber;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String phpPage = params[0];

        try {
            ServerConnector serverConnector = new ServerConnector(phpPage);

            serverConnector.addPostData("event_number", mEventNumber);
            serverConnector.addDelimiter();

            serverConnector.writePostData();
            serverConnector.finish();

            return serverConnector.response();

        } catch (MalformedURLException e) {
            return new String("NullPoint: " + e.getMessage());
        } catch (IOException e) {
            return new String("Error: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d(TAG, "response - " + result);

        try {
            int resultCode = Integer.parseInt(result);
            if (resultCode == 0) {
                Intent intent = new Intent(mContext, Activity_Owner_Add_Event.class);
                intent.putExtra("event_number", mEventNumber);
                mContext.startActivity(intent);
            } else if (resultCode == 1) {
                Toast.makeText(mContext, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == 2) {
                Toast.makeText(mContext, "이벤트 참여자가 있어 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Log.d(TAG, "NumberFormatException");
        }
    }
}

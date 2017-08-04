package com.example.win.a2vent;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import static com.example.win.a2vent.Activity_User_Login.toast;
import static com.example.win.a2vent.User_Entry_Adapter.delete_event_number;

/**
 * Created by Administrator on 2017-08-04.
 */

public class DeleteUserEntry extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String serverURL = "2ventDeleteUserEntry.php";

        try {
            ServerConnector serverConnector = new ServerConnector(serverURL);

            serverConnector.addPostData("event_number", params[0]);
            serverConnector.addPostData("id", params[1]);

            serverConnector.addDelimiter();
            serverConnector.writePostData();
            serverConnector.finish();

            return serverConnector.response();

        } catch (Exception e) {
            Log.d("DB", "DeleteUserEntry Error ", e);

            return new String("Error: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("DeleteUserEntry", "response - " + result);

        setDelete(result);
    }

    private void setDelete(String result) {
        if (result.equals("Delete Done")) {
            Activity_User_Entry_List.mCategory.remove(delete_event_number);
            Activity_User_Entry_List.rAdapter_UserEntryList.notifyDataSetChanged();

            //TODO 새로고침 하는거
        }
    }

} // UserEntry에서 삭제하는 AsyncTask

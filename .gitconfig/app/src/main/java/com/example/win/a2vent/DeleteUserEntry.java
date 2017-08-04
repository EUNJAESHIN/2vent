package com.example.win.a2vent;

import android.os.AsyncTask;
import android.util.Log;
import static com.example.win.a2vent.User_Entry_Adapter.delete_event_number;

/**
 * Created by EUNJAESHIN on 2017-08-04.
 * UserEntry에서 삭제하는 AsyncTask
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
        if (result.equals("Delete Done")) {
            Activity_User_Entry_List.mCategory.remove(delete_event_number);
            Activity_User_Entry_List.rAdapter_UserEntryList.notifyDataSetChanged();
        }
    }

}

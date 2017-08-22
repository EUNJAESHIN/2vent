package com.example.win.a2vent.onwer.entry_list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.win.a2vent.R;
import com.example.win.a2vent.util.ServerConnector;

import java.util.ArrayList;

/**
 * Created by win on 2017-08-01.
 */

public class Owner_Entry_Adapter extends RecyclerView.Adapter<Owner_Entry_Holder> implements Filterable {

    private final String TAG = "테스트";

    ArrayList<Owner_Entry_Item> entryList, filterList;
    private Context mContext;
    private SearchFilter filter;
    private String mEvent_number;

    public Owner_Entry_Adapter(ArrayList<Owner_Entry_Item> arrayList, Context context, String event_number) {
        entryList = arrayList;
        filterList = arrayList;
        mContext = context;
        mEvent_number = event_number;
    }

    @Override
    public Owner_Entry_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_entry_list_item, parent, false);
        Owner_Entry_Holder holder = new Owner_Entry_Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Owner_Entry_Holder holder, final int position) {
        holder.getTvNo().setText(entryList.get(position).getNo());
        holder.getTvID().setText(entryList.get(position).getID());
        holder.getTvName().setText(entryList.get(position).getName());
        holder.getTvSex().setText(entryList.get(position).getSex());
        holder.getTvPhoneNo().setText(entryList.get(position).getPhoneNo());
        holder.getTvAddress().setText(entryList.get(position).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                if (entryList.get(position).getIs_entry().equals("0")) {
                    builder.setMessage("참여확인 하시겠습니까?").setCancelable(false)
                            .setPositiveButton("참여확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new InputIsEntry().execute("2ventInputIsEntry.php", mEvent_number, entryList.get(position).getID(), "1");
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                } else if (entryList.get(position).getIs_entry().equals("1")) {
                    builder.setMessage("참여확인을 되돌리겠습니까?").setCancelable(false)
                            .setPositiveButton("되돌리기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new InputIsEntry().execute("2ventInputIsEntry.php", mEvent_number, entryList.get(position).getID(), "0");
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                }
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        if (entryList.get(position).getIs_entry().equals("0")) {
            holder.getCvEntry().setCardBackgroundColor(Color.rgb(255, 240, 240));
        } else if (entryList.get(position).getIs_entry().equals("1")) {
            holder.getCvEntry().setCardBackgroundColor(Color.rgb(240, 255, 255));
        }
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SearchFilter(filterList, this);
        }
        return filter;
    }

    private class InputIsEntry extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String phpPage = params[0];
            String event_number = params[1];
            String id = params[2];
            String is_entry = params[3];

            try {
                ServerConnector serverConnector = new ServerConnector(phpPage);

                serverConnector.addPostData("event_number", event_number);
                serverConnector.addPostData("id", id);
                serverConnector.addPostData("is_entry", is_entry);
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

            if (result.equals("SQL문 처리 성공")) {
                Intent intent = new Intent("com.example.win.a2vent.Activity_Owner_Entry_List_Receiver");
                intent.putExtra("input_finish", "success");
                mContext.sendBroadcast(intent);
            }
        }
    }
}

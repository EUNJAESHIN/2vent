package com.example.win.a2vent.onwer.entry_list;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.win.a2vent.R;

/**
 * Created by win on 2017-08-01.
 */

public class Owner_Entry_Holder extends RecyclerView.ViewHolder {

    private TextView tvNo;
    private TextView tvID;
    private TextView tvName;
    private TextView tvSex;
    private TextView tvPhoneNo;
    private TextView tvAddress;
    private CardView cvEntry;


    public Owner_Entry_Holder(View itemView) {
        super(itemView);
        tvNo = (TextView) itemView.findViewById(R.id.tvNo);
        tvID = (TextView) itemView.findViewById(R.id.tvID);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvSex = (TextView) itemView.findViewById(R.id.tvSex);
        tvPhoneNo = (TextView) itemView.findViewById(R.id.tvPhoneNo);
        tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
        cvEntry = (CardView) itemView.findViewById(R.id.cvEntry);
    }

    public TextView getTvNo() {
        return tvNo;
    }

    public TextView getTvID() {
        return tvID;
    }

    public TextView getTvName() {
        return tvName;
    }

    public TextView getTvSex() {
        return tvSex;
    }

    public TextView getTvPhoneNo() {
        return tvPhoneNo;
    }

    public TextView getTvAddress() {
        return tvAddress;
    }

    public CardView getCvEntry() {
        return cvEntry;
    }
}

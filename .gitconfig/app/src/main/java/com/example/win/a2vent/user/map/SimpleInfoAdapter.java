package com.example.win.a2vent.user.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.win.a2vent.R;

import java.util.ArrayList;

/**
 * Created by win on 2017-08-21.
 */

public class SimpleInfoAdapter extends RecyclerView.Adapter<SimpleInfoHolder> {

    private final String TAG = "테스트";

    private ArrayList<Integer> mEventList = new ArrayList<>();
    private int mEventNumber = 0;
    private int mCount;
    private int mIndex;

    public SimpleInfoAdapter(int count, int index) {
        mCount = count;
        mIndex = index;
    }

    @Override
    public SimpleInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_simpleinfo_item, parent, false);
        SimpleInfoHolder holder = new SimpleInfoHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleInfoHolder holder, int position) {
        if (position == mIndex) {
            holder.getIvCircleSimpleSelect().setImageResource(R.drawable.circle_1);
        } else {
            holder.getIvCircleSimpleSelect().setImageResource(R.drawable.circle_0);
        }
    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}

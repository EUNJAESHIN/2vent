package com.example.win.a2vent.user.map;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.win.a2vent.R;

/**
 * Created by win on 2017-08-21.
 */

public class SimpleInfoHolder extends RecyclerView.ViewHolder {

    private ImageView ivCircleSimpleSelect;

    public SimpleInfoHolder(View itemView) {
        super(itemView);
        ivCircleSimpleSelect = (ImageView) itemView.findViewById(R.id.circle_simple_select);
    }

    public ImageView getIvCircleSimpleSelect() {
        return ivCircleSimpleSelect;
    }
}

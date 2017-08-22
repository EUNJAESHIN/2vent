package com.example.win.a2vent.onwer.entry_list;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by win on 2017-08-03.
 */

public class SearchFilter extends Filter {

    private Owner_Entry_Adapter adapter;
    private ArrayList<Owner_Entry_Item> filterList;

    public SearchFilter(ArrayList<Owner_Entry_Item> filterList, Owner_Entry_Adapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {

            constraint = constraint.toString();

            ArrayList<Owner_Entry_Item> filteredEntry = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getPhoneNo().contains(constraint) || filterList.get(i).getSex().contains(constraint)) {
                    filteredEntry.add(filterList.get(i));
                }
            }
            results.count = filteredEntry.size();
            results.values = filteredEntry;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.entryList = (ArrayList<Owner_Entry_Item>) results.values;

        adapter.notifyDataSetChanged();
    }
}

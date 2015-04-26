package com.example.andrewhinh.pebblespeak.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrewhinh.pebblespeak.R;

import java.util.ArrayList;

/**
 * Created by Alec on 4/25/15.
 */
public class IntentAdapter extends ArrayAdapter<Pair<String, Intent>> {

    public IntentAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Pair<String, Intent> user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.actionName);

        // Populate the data into the template view using the data object
        tvName.setText(user.first);

        // Return the completed view to render on screen
        return convertView;
    }
}

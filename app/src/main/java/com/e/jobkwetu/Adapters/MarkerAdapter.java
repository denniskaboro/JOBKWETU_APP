package com.e.jobkwetu.Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.e.jobkwetu.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private final Activity context;


    public MarkerAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        //int position = Integer.parseInt(marker.getSnippet());

        View infoWindow = context.getLayoutInflater().inflate(R.layout.map_info_window, null);
        TextView title = infoWindow.findViewById(R.id.tv_name);
        TextView snippet = infoWindow.findViewById(R.id.tv_reviews);

        //com.e.jobkwetu.Model.Marker locationdat = (com.e.jobkwetu.Model.Marker) marker.getTag();
        //title.setText(locationdat.getName());
        // snippet.setText(locationdat.getType());


        //Todo your code here

        return infoWindow;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }

}
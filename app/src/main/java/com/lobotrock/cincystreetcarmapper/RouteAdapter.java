package com.lobotrock.cincystreetcarmapper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteAdapter extends ArrayAdapter<Route> {

    Context context;
    int layoutResourceId;
    Route routes[] = null;

    public RouteAdapter(Context context, int layoutResourceId, Route[] routes){
        super(context, layoutResourceId, routes);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.routes = routes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RouteHolder holder;

        Route route = routes[position];
        //TODO: If there are no results return nothing

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RouteHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else{
            holder = (RouteHolder)row.getTag();
        }

        if(route != null){
            holder.txtTitle.setText(route.title);
            holder.imgIcon.setImageResource(route.icon);
        }
        else{
            row.setVisibility(View.GONE);
        }

        return row;
    }

    static class RouteHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}

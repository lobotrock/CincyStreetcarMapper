package com.lobotrock.cincystreetcarmapper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class RouteOnClickListener implements AdapterView.OnItemClickListener {

    private ListView routeList;
    private Activity activity;

    public RouteOnClickListener(Activity activity, ListView routeList){
        super();
        this.activity = activity;
        this.routeList = routeList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        // ListView Clicked item value
        Route selectedRoute = (Route) routeList.getItemAtPosition(position);

        //Saving most recent choice
        RecentRoutes recentRoutes = new RecentRoutes(activity);
        recentRoutes.saveRecentSelection(selectedRoute);

        //Loading next activity
        Intent intent = new Intent(activity, MapActivity.class);
        intent.putExtra(MainActivity.ROUTE, selectedRoute.searchText);
        activity.startActivity(intent);
    }
}

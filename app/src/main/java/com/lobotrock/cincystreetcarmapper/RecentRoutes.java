package com.lobotrock.cincystreetcarmapper;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class RecentRoutes {

    Activity contextActivity;
    //The name of the recent routes stored in shared prefs
    private final String RECENT_ROUTES = "RecentRoutes";
    private final int MAX_RECENT_ROUTES = 3;

    public RecentRoutes(Activity activity){
        this.contextActivity = activity;
    }

    private SharedPreferences getSharedPrefrences(){
        return contextActivity.getPreferences(Activity.MODE_PRIVATE);
    }

    private Route[] createRecentRoutes(){
        Route[] recentRoutes = new Route[MAX_RECENT_ROUTES];

        SharedPreferences.Editor editor = getSharedPrefrences().edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentRoutes);
        editor.putString(RECENT_ROUTES, json);
        editor.commit();

        return recentRoutes;
    }

    public Route[] getRecentRoutes(){
        //Getting shared preferences
        SharedPreferences sharedPreferences = getSharedPrefrences();

        //Retreiving the list of most recent routes
        Gson gson = new Gson();
        String json = sharedPreferences.getString(RECENT_ROUTES, "");
        Route[] recentRoutes = gson.fromJson(json, Route[].class);

        //If recent routes doesn't exist, it needs to be created
        if(recentRoutes == null){
            recentRoutes = createRecentRoutes();
        }

        return recentRoutes;
    }

    public void saveRecentSelection(Route selectedRoute){
        Route[] recentRoutes = getRecentRoutes();
        //checking to see if the route exists in the list
        boolean routeAlreadyRecent = false;
        for(Route route : recentRoutes){
            if(route != null && selectedRoute.searchText.equals(route.searchText)){
                routeAlreadyRecent = true;
            }
        }

        //If the route is not in the recent list, update the recent list
        if(!routeAlreadyRecent) {
            //Shift everything down in the array
            recentRoutes[2] = recentRoutes[1];
            recentRoutes[1] = recentRoutes[0];
            recentRoutes[0] = selectedRoute;

            //Opening the shared preference editor
            SharedPreferences.Editor editor = getSharedPrefrences().edit();
            Gson gson = new Gson();
            String json = gson.toJson(recentRoutes);
            editor.putString(RECENT_ROUTES, json);
            editor.commit();
        }
    }
}

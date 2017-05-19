package com.lobotrock.cincystreetcarmapper;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class RecentRoutes {

    private Activity contextActivity;
    //The name of the recent routes stored in shared prefs
    private final String RECENT_ROUTES = "RecentRoutes";
    private final int MAX_RECENT_ROUTES = 3;

    /**
     * Constructor needs activity so it can access the SharedPrefrences
     *
     * @param activity current activity
     */
    public RecentRoutes(Activity activity){
        this.contextActivity = activity;
    }

    /**
     * getSharedPreferences provides a consistant way the preferences are retrieved each time
     *
     * @return sharedPreferences
     */
    private SharedPreferences getSharedPreferences(){
        return contextActivity.getPreferences(Activity.MODE_PRIVATE);
    }

    /**
     * Creating the Recent Routes in Shared Preferences if it doesn't exists
     *
     * @return newly created routes array
     */
    private Route[] createRecentRoutes(){
        Route[] recentRoutes = new Route[0];

        SharedPreferences.Editor editor = getSharedPreferences().edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentRoutes);
        editor.putString(RECENT_ROUTES, json);
        editor.apply();

        return recentRoutes;
    }

    /**
     * Getting the recent routes stored in the shared preferences
     *
     * @return route array of recent routes
     */
    public Route[] getRecentRoutes(){
        //Getting shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences();

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

    /**
     * Used for adding a route to the recent routes and saving it to the shared preferences
     *
     * @param selectedRoute new route to be added to recent routes
     */
    public void saveRecentSelection(Route selectedRoute) {
        Route[] recentRoutes = getRecentRoutes();
        //checking to see if the route exists in the list
        boolean routeAlreadyRecent = false;
        for (Route route : recentRoutes) {
            if (route != null && selectedRoute.searchText.equals(route.searchText)) {
                routeAlreadyRecent = true;
            }
        }

        //If the route is not in the recent list, update the recent list
        if (!routeAlreadyRecent) {
            Route[] newRecentRoutes;

            //Check the size of the array
            if (recentRoutes.length < MAX_RECENT_ROUTES) {
                newRecentRoutes = new Route[recentRoutes.length + 1];
            } else {
                newRecentRoutes = new Route[recentRoutes.length];
            }

            //Shift everything down in the array
            for (int i = 0; i < recentRoutes.length && i + 1 < newRecentRoutes.length; i++) {
                newRecentRoutes[i + 1] = recentRoutes[i];
            }

            //Add the new route
            newRecentRoutes[0] = selectedRoute;

            //Opening the shared preference editor
            SharedPreferences.Editor editor = getSharedPreferences().edit();
            Gson gson = new Gson();
            String json = gson.toJson(newRecentRoutes);
            editor.putString(RECENT_ROUTES, json);
            editor.apply();
        }
    }
}

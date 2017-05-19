package com.lobotrock.cincystreetcarmapper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //Name of the key for the route being passed to the Map Activity
    final public static String ROUTE = "route";

    /**
     * Setting up the view for the main activity
     */
    protected void createListView() {
        //Finding recent routes
        RecentRoutes recentRoutes = new RecentRoutes(this);
        Route[] recentValues = recentRoutes.getRecentRoutes();

        if(!loadRecentList()) {
            setContentView(R.layout.activity_main_without_recent);
        }

        final ListView routeList = (ListView)findViewById(R.id.full_list);

        //Adding header for routeList
        TextView routeHeader = new TextView(this);
        routeHeader.setText(R.string.all_header);
        routeList.addHeaderView(routeHeader);

        RouteAdapter adapter = new RouteAdapter(this, R.layout.listview_item_row, getAllRoutes());
        routeList.setAdapter(adapter);
        routeList.setOnItemClickListener(new RouteOnClickListener(this, routeList));
    }

    /**
     * Searches user preferences to find if any recent routes have been selected and
     * loads the list of recently selected routes with the appropriate layout.
     *
     * @return true is recent routes are found, otherwise false
     */
    private boolean loadRecentList(){
        RecentRoutes recentRoutes = new RecentRoutes(this);
        Route[] recentValues = recentRoutes.getRecentRoutes();

        if(recentValues.length > 0) {
            setContentView(R.layout.activity_main);

            final ListView recentList = (ListView)findViewById(R.id.recent_list);

            //Adding header for recentList
            TextView recentHeader = new TextView(this);
            recentHeader.setText(R.string.recent_header);
            recentList.addHeaderView(recentHeader);

            RouteAdapter recentAdapter = new RouteAdapter(this, R.layout.listview_item_row, recentValues);
            recentList.setAdapter(recentAdapter);
            recentList.setOnItemClickListener(new RouteOnClickListener(this, recentList));
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * haveNetworkConnection will test the network connections.
     *
     *  Requires android.permission.ACCESS_NETWORK_STATE
     * @return returns false if no network connection is found,
     *  and return true a network connection is found.
     */
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                if (netInfo.isConnected()) {
                    haveConnectedWifi = true;
                }
            }
            if (netInfo != null && netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (netInfo.isConnected()) {
                    haveConnectedMobile = true;
                }
            }

        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * getAllRoutes currently reads from data/routes.txt to find all the routes
     *  The route information was recorded on May 1st, 2017.  Ideally this method
     *  should read from the bus tracker website and compile the list.
     *
     * @return array of all the possible bus routes
     */
    private Route[] getAllRoutes() {
        //Putting routes in a list rather than an array,
        // since the number of routes is unknown at this time
        List<Route> routesList = new ArrayList<>();

        BufferedReader reader;
        InputStreamReader inputStream;

        try{
            //Reading from the data/routes.txt which is in the format:
            //  <icon>,<route_title>,<route_search_text>
            final InputStream file = getAssets().open("data/routes.txt");
            inputStream = new InputStreamReader(file);
            reader = new BufferedReader(inputStream);

            // Reading the routes files line by line
            String line;
            while((line = reader.readLine()) != null){

                String[] lineData = line.split(",");

                if (lineData.length != 3) {
                    //Error in data
                    Log.d(this.getClass().getName(), "data/routes.txt has invalid data: " + line);
                }
                if ("streetcar".equals(lineData[0])) {
                    routesList.add(new Route(R.drawable.streetcar, lineData[1], lineData[2]));
                } else if ("bus".equals(lineData[0])) {
                    routesList.add(new Route(R.drawable.bus, lineData[1], lineData[2]));
                } else if ("express_bus".equals(lineData[0])) {
                    routesList.add(new Route(R.drawable.express_bus, lineData[1], lineData[2]));
                }

            }

            inputStream.close();
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        Route[] routeArray = new Route[routesList.size()];
        return routesList.toArray(routeArray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getClass().getName(), "Calling onCreate for Main Activity");
        setContentView(R.layout.activity_main);

        if(haveNetworkConnection()) {
            createListView();
        }
        else{
            //Showing error dialog when no network connection is found.
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Error")
                    .setMessage("No Network Connection Found!")
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    }).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        this.finish();

    }
}

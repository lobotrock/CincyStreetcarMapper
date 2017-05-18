package com.lobotrock.cincystreetcarmapper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    //Name of the key for the route being passed to the Map Activity
    final public static String ROUTE = "route";

    protected void createListView() {
        //Finding recent routes
        RecentRoutes recentRoutes = new RecentRoutes(this);
        Route[] recentValues = recentRoutes.getRecentRoutes();

        if(recentValues[0] != null) {
            setContentView(R.layout.activity_main);

            final ListView recentList = (ListView)findViewById(R.id.recent_list);

            //Adding header for recentList
            TextView recentHeader = new TextView(this);
            recentHeader.setText("Recent Routes");
            recentList.addHeaderView(recentHeader);

            RouteAdapter recentAdapter = new RouteAdapter(this, R.layout.listview_item_row, recentValues);
            recentList.setAdapter(recentAdapter);
            recentList.setOnItemClickListener(new RouteOnClickListener(this, recentList));
        }
        else{
            setContentView(R.layout.activity_main_without_recent);
        }

        final ListView routeList = (ListView)findViewById(R.id.full_list);

        //Adding header for routeList
        TextView routeHeader = new TextView(this);
        routeHeader.setText("All Routes");
        routeList.addHeaderView(routeHeader);

        RouteAdapter adapter = new RouteAdapter(this, R.layout.listview_item_row, getAllRoutes());
        routeList.setAdapter(adapter);
        routeList.setOnItemClickListener(new RouteOnClickListener(this, routeList));
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

    private Route[] getAllRoutes() {
        //These are all the possible routes as of May 1st, 2017
        // ideally these routes should be read from the bustracker site
        // rather than hard coding the values here.
        return new Route[]{
                new Route(R.drawable.streetcar, "Streetcar", "Streetcar"),    //I included the streetcar inbound and outbound in one choice
                new Route(R.drawable.bus, "1 - Museum Center Mt. Adams Eden Park - Eastbound", "1 - Museum Center Mt. Adams Eden Park - Eastbound"),
                new Route(R.drawable.bus, "1 - Museum Center Mt. Adams Eden Park - Westbound", "1 - Museum Center Mt. Adams Eden Park - Westbound"),
                new Route(R.drawable.express_bus, "2 - Madeira - Kenwood Express - Inbound", "2 - Madeira - Kenwood Express - Inbound"),
                new Route(R.drawable.express_bus, "2 - Madeira - Kenwood Express - Outbound", "2 - Madeira - Kenwood Express - Outbound"),
                new Route(R.drawable.express_bus, "3 - Montgomery Express - Job Connection - Inbound", "3 - Montgomery Express - Job Connection - Inbound"),
                new Route(R.drawable.express_bus, "3 - Montgomery Express - Job Connection - Outbound", "3 - Montgomery Express - Job Connection - Outbound"),
                new Route(R.drawable.bus, "4 - Kenwood - Blue Ash - Ridge Rd - Inbound", "4 - Kenwood - Blue Ash - Ridge Rd - Inbound"),
                new Route(R.drawable.bus, "4 - Kenwood - Blue Ash - Ridge Rd - Outbound", "4 - Kenwood - Blue Ash - Ridge Rd - Outbound"),
                new Route(R.drawable.bus, "6 - Queen City - Westwood - Inbound", "6 - Queen City - Westwood - Inbound"),
                new Route(R.drawable.bus, "6 - Queen City - Westwood - Outbound", "6 - Queen City - Westwood - Outbound"),
                new Route(R.drawable.bus, "11 - Madison Road - Hyde Park - Erie - Inbound", "11 - Madison Road - Hyde Park - Erie - Inbound"),
                new Route(R.drawable.bus, "11 - Madison Road - Hyde Park - Erie - Outbound", "11 - Madison Road - Hyde Park - Erie - Outbound"),
                new Route(R.drawable.express_bus, "12 - Madisonville Express - Inbound", "12 - Madisonville Express - Inbound"),
                new Route(R.drawable.express_bus, "12 - Madisonville Express - Outbound", "12 - Madisonville Express - Outbound"),
                new Route(R.drawable.express_bus, "14 - Forest Park Express - Inbound", "14 - Forest Park Express - Inbound"),
                new Route(R.drawable.express_bus, "14 - Forest Park Express - Outbound", "14 - Forest Park Express - Outbound"),
                new Route(R.drawable.express_bus, "15 - Mt Healthy Express - AM Inbound", "15 - Mt Healthy Express - AM Inbound"),
                new Route(R.drawable.express_bus, "15 - Mt Healthy Express - PM Outbound", "15 - Mt Healthy Express - PM Outbound"),
                new Route(R.drawable.bus, "16 - Mt Healthy - Spring Grove Ave - Inbound", "16 - Mt Healthy - Spring Grove Ave - Inbound"),
                new Route(R.drawable.bus, "16 - Mt Healthy - Spring Grove Ave - Outbound", "16 - Mt Healthy - Spring Grove Ave - Outbound"),
                new Route(R.drawable.bus, "17 - Hamilton Avenue - Inbound", "17 - Hamilton Avenue - Inbound"),
                new Route(R.drawable.bus, "17 - Hamilton Avenue - Outbound", "17 - Hamilton Avenue - Outbound"),
                new Route(R.drawable.bus, "19 - Colerain - Northgate - Inbound", "19 - Colerain - Northgate - Inbound"),
                new Route(R.drawable.bus, "19 - Colerain - Northgate - Outbound", "19 - Colerain - Northgate - Outbound"),
                new Route(R.drawable.bus, "20 - Winton Rd - Tri - County - Inbound", "20 - Winton Rd - Tri - County - Inbound"),
                new Route(R.drawable.bus, "20 - Winton Rd - Tri - County - Outbound", "20 - Winton Rd - Tri - County - Outbound"),
                new Route(R.drawable.bus, "21 - Westwood - Harrison Avenue - Inbound", "21 - Westwood - Harrison Avenue - Inbound"),
                new Route(R.drawable.bus, "21 - Westwood - Harrison Avenue - Outbound", "21 - Westwood - Harrison Avenue - Outbound"),
                new Route(R.drawable.express_bus, "23 - Tri - County - Forest Park Express - Inbound", "23 - Tri - County - Forest Park Express - Inbound"),
                new Route(R.drawable.express_bus, "23 - Tri - County - Forest Park Express - Outbound", "23 - Tri - County - Forest Park Express - Outbound"),
                new Route(R.drawable.bus, "24 - Mt Washington - Uptown - Inbound", "24 - Mt Washington - Uptown - Inbound"),
                new Route(R.drawable.bus, "24 - Mt Washington - Uptown - Outbound", "24 - Mt Washington - Uptown - Outbound"),
                new Route(R.drawable.express_bus, "25 - Mt Lookout - Hyde Park Express - Inbound", "25 - Mt Lookout - Hyde Park Express - Inbound"),
                new Route(R.drawable.express_bus, "25 - Mt Lookout - Hyde Park Express - Outbound", "25 - Mt Lookout - Hyde Park Express - Outbound"),
                new Route(R.drawable.bus, "27 - South Cumminsville - Linn Street - Inbound", "27 - South Cumminsville - Linn Street - Inbound"),
                new Route(R.drawable.bus, "27 - South Cumminsville - Linn Street - Outbound", "27 - South Cumminsville - Linn Street - Outbound"),
                new Route(R.drawable.bus, "28 - East End - Fairfax - Milford - Inbound", "28 - East End - Fairfax - Milford - Inbound"),
                new Route(R.drawable.bus, "28 - East End - Fairfax - Milford - Outbound", "28 - East End - Fairfax - Milford - Outbound"),
                new Route(R.drawable.express_bus, "29 - Milford Express - Inbound", "29 - Milford Express - Inbound"),
                new Route(R.drawable.express_bus, "29 - Milford Express - Outbound", "29 - Milford Express - Outbound"),
                new Route(R.drawable.express_bus, "30 - Beechmont - 8 Mile Express - Inbound", "30 - Beechmont - 8 Mile Express - Inbound"),
                new Route(R.drawable.express_bus, "30 - Beechmont - 8 Mile Express - Outbound", "30 - Beechmont - 8 Mile Express - Outbound"),
                new Route(R.drawable.bus, "31 - Crosstown - Queensgate - Evanston - Eastbound", "31 - Crosstown - Queensgate - Evanston - Eastbound"),
                new Route(R.drawable.bus, "31 - Crosstown - Queensgate - Evanston - Westbound", "31 - Crosstown - Queensgate - Evanston - Westbound"),
                new Route(R.drawable.bus, "32 - Price Hill - Delhi - Covedale - Inbound", "32 - Price Hill - Delhi - Covedale - Inbound"),
                new Route(R.drawable.bus, "32 - Price Hill - Delhi - Covedale - Outbound", "32 - Price Hill - Delhi - Covedale - Outbound"),
                new Route(R.drawable.bus, "33 - Glenway Avenue - Inbound", "33 - Glenway Avenue - Inbound"),
                new Route(R.drawable.bus, "33 - Glenway Avenue - Outbound", "33 - Glenway Avenue - Outbound"),
                new Route(R.drawable.bus, "38 - Glenway Crossing - Uptown - Inbound", "38 - Glenway Crossing - Uptown - Inbound"),
                new Route(R.drawable.bus, "38 - Glenway Crossing - Uptown - Outbound", "38 - Glenway Crossing - Uptown - Outbound"),
                new Route(R.drawable.express_bus, "40 - Montana Avenue Express - Inbound", "40 - Montana Avenue Express - Inbound"),
                new Route(R.drawable.express_bus, "40 - Montana Avenue Express - Outbound", "40 - Montana Avenue Express - Outbound"),
                new Route(R.drawable.bus, "41 - Crosstown: Westwood - Oakley - Eastbound", "41 - Crosstown: Westwood - Oakley - Eastbound"),
                new Route(R.drawable.bus, "41 - Crosstown: Westwood - Oakley - Westbound", "41 - Crosstown: Westwood - Oakley - Westbound"),
                new Route(R.drawable.express_bus, "42 - West Chester Express - Inbound", "42 - West Chester Express - Inbound"),
                new Route(R.drawable.express_bus, "42 - West Chester Express - Outbound", "42 - West Chester Express - Outbound"),
                new Route(R.drawable.bus, "43 - Reading Rd-Winton Hills-Bond Hill - Inbound", "43 - Reading Rd-Winton Hills-Bond Hill - Inbound"),
                new Route(R.drawable.bus, "43 - Reading Rd-Winton Hills-Bond Hill - Outbound", "43 - Reading Rd-Winton Hills-Bond Hill - Outbound"),
                new Route(R.drawable.bus, "46 - Avondale - Corryville - Zoo - Inbound", "46 - Avondale - Corryville - Zoo - Inbound"),
                new Route(R.drawable.bus, "46 - Avondale - Corryville - Zoo - Outbound", "46 - Avondale - Corryville - Zoo - Outbound"),
                new Route(R.drawable.bus, "49 - Queensgate - Fairmount - Inbound", "49 - Queensgate - Fairmount - Inbound"),
                new Route(R.drawable.bus, "49 - Queensgate - Fairmount - Outbound", "49 - Queensgate - Fairmount - Outbound"),
                new Route(R.drawable.bus, "50 - River Road - Saylor Park - Inbound", "50 - River Road - Saylor Park - Inbound"),
                new Route(R.drawable.bus, "50 - River Road - Saylor Park - Outbound", "50 - River Road - Saylor Park - Outbound"),
                new Route(R.drawable.bus, "51 - Glenway Crossing - Hyde Park Oakley - Westbound", "51 - Glenway Crossing - Hyde Park Oakley - Westbound"),
                new Route(R.drawable.bus, "51 - Glenway Crossing - Hyde Park Oakley - Eastbound", "51 - Glenway Crossing - Hyde Park Oakley - Eastbound"),
                new Route(R.drawable.express_bus, "52 - Harrison Express - Inbound", "52 - Harrison Express - Inbound"),
                new Route(R.drawable.express_bus, "52 - Harrison Express - Outbound", "52 - Harrison Express - Outbound"),
                new Route(R.drawable.bus, "64 - Glenway Crossing Westwood McMicken - Inbound", "64 - Glenway Crossing Westwood McMicken - Inbound"),
                new Route(R.drawable.bus, "64 - Glenway Crossing Westwood McMicken - Outbound", "64 - Glenway Crossing Westwood McMicken - Outbound"),
                new Route(R.drawable.bus, "67 - Sharonville Blue Ash Job Connection - Inbound", "67 - Sharonville Blue Ash Job Connection - Inbound"),
                new Route(R.drawable.bus, "67 - Sharonville Blue Ash Job Connection - Outbound", "67 - Sharonville Blue Ash Job Connection - Outbound"),
                new Route(R.drawable.express_bus, "71 - Warren County Express Kings Island - Inbound", "71 - Warren County Express Kings Island - Inbound"),
                new Route(R.drawable.express_bus, "71 - Warren County Express Kings Island - Outbound", "71 - Warren County Express Kings Island - Outbound"),
                new Route(R.drawable.bus, "72 - Kings Island Direct - Inbound", "72 - Kings Island Direct - Inbound"),
                new Route(R.drawable.bus, "72 - Kings Island Direct - Outbound", "72 - Kings Island Direct - Outbound"),
                new Route(R.drawable.express_bus, "74 - Colerain Express - Inbound", "74 - Colerain Express - Inbound"),
                new Route(R.drawable.express_bus, "74 - Colerain Express - Outbound", "74 - Colerain Express - Outbound"),
                new Route(R.drawable.express_bus, "75 - Anderson Express - Inbound", "75 - Anderson Express - Inbound"),
                new Route(R.drawable.express_bus, "75 - Anderson Express - Outbound", "75 - Anderson Express - Outbound"),
                new Route(R.drawable.express_bus, "77 - Delhi Express - Glenway Crossing - Inbound", "77 - Delhi Express - Glenway Crossing - Inbound"),
                new Route(R.drawable.express_bus, "77 - Delhi Express - Glenway Crossing - Outbound", "77 - Delhi Express - Glenway Crossing - Outbound"),
                new Route(R.drawable.bus, "78 - Tri - County - Lincoln Heights - Inbound", "78 - Tri - County - Lincoln Heights - Inbound"),
                new Route(R.drawable.bus, "78 - Tri - County - Lincoln Heights - Outbound", "78 - Tri - County - Lincoln Heights - Outbound"),
                new Route(R.drawable.express_bus, "81 - Mt. Washington Express - Inbound", "81 - Mt. Washington Express - Inbound"),
                new Route(R.drawable.express_bus, "81 - Mt. Washington Express - Outbound", "81 - Mt. Washington Express - Outbound"),
                new Route(R.drawable.express_bus, "82 - Eastgate Express - Inbound", "82 - Eastgate Express - Inbound"),
                new Route(R.drawable.express_bus, "82 - Eastgate Express - Outbound", "82 - Eastgate Express - Outbound"),
                new Route(R.drawable.bus, "85 - Riverfront Parking Shuttle - AM Inbound", "85 - Riverfront Parking Shuttle - AM Inbound"),
                new Route(R.drawable.bus, "85 - Riverfront Parking Shuttle - PM Outbound", "85 - Riverfront Parking Shuttle - PM Outbound"),
                new Route(R.drawable.bus, "90 - MetroPlus - Kenwood - Inbound", "90 - MetroPlus - Kenwood - Inbound"),
                new Route(R.drawable.bus, "90 - MetroPlus - Kenwood - Outbound", "90 - MetroPlus - Kenwood - Outbound")
        };
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
}

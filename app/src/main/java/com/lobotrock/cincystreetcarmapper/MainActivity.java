package com.lobotrock.cincystreetcarmapper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;


public class MainActivity extends AppCompatActivity {
    //Constants
    //Name of the key for the route being passed to the Map Activity
    final public static String ROUTE = "route";

    protected void createListView() {
        setContentView(R.layout.activity_main);
        final ListView routeList = (ListView)findViewById(R.id.list);

        //These are all the possible routes as of May 1st, 2017
        // ideally these routes should be read from the bustracker site
        // rather than hard coding the values here.
        String[] values = new String[]{
                "Streetcar",    //I included the streetcar inbound and outbound in one choice
                "1 - Museum Center Mt. Adams Eden Park - Eastbound",
                "1 - Museum Center Mt. Adams Eden Park - Westbound",
                "2 - Madeira - Kenwood Express - Inbound",
                "2 - Madeira - Kenwood Express - Outbound",
                "3 - Montgomery Express - Job Connection - Inbound",
                "3 - Montgomery Express - Job Connection - Outbound",
                "4 - Kenwood - Blue Ash - Ridge Rd - Inbound",
                "4 - Kenwood - Blue Ash - Ridge Rd - Outbound",
                "6 - Queen City - Westwood - Inbound",
                "6 - Queen City - Westwood - Outbound",
                "11 - Madison Road - Hyde Park - Erie - Inbound",
                "11 - Madison Road - Hyde Park - Erie - Outbound",
                "12 - Madisonville Express - Inbound",
                "12 - Madisonville Express - Outbound",
                "14 - Forest Park Express - Inbound",
                "14 - Forest Park Express - Outbound",
                "15 - Mt Healthy Express - AM Inbound",
                "15 - Mt Healthy Express - PM Outbound",
                "16 - Mt Healthy - Spring Grove Ave - Inbound",
                "16 - Mt Healthy - Spring Grove Ave - Outbound",
                "17 - Hamilton Avenue - Inbound",
                "17 - Hamilton Avenue - Outbound",
                "19 - Colerain - Northgate - Inbound",
                "19 - Colerain - Northgate - Outbound",
                "20 - Winton Rd - Tri - County - Inbound",
                "20 - Winton Rd - Tri - County - Outbound",
                "21 - Westwood - Harrison Avenue - Inbound",
                "21 - Westwood - Harrison Avenue - Outbound",
                "23 - Tri - County - Forest Park Express - Inbound",
                "23 - Tri - County - Forest Park Express - Outbound",
                "24 - Mt Washington - Uptown - Inbound",
                "24 - Mt Washington - Uptown - Outbound",
                "25 - Mt Lookout - Hyde Park Express - Inbound",
                "25 - Mt Lookout - Hyde Park Express - Outbound",
                "27 - South Cumminsville - Linn Street - Inbound",
                "27 - South Cumminsville - Linn Street - Outbound",
                "28 - East End - Fairfax - Milford - Inbound",
                "28 - East End - Fairfax - Milford - Outbound",
                "29 - Milford Express - Inbound",
                "29 - Milford Express - Outbound",
                "30 - Beechmont - 8 Mile Express - Inbound",
                "30 - Beechmont - 8 Mile Express - Outbound",
                "31 - Crosstown - Queensgate - Evanston - Eastbound",
                "31 - Crosstown - Queensgate - Evanston - Westbound",
                "32 - Price Hill - Delhi - Covedale - Inbound",
                "32 - Price Hill - Delhi - Covedale - Outbound",
                "33 - Glenway Avenue - Inbound",
                "33 - Glenway Avenue - Outbound",
                "38 - Glenway Crossing - Uptown - Inbound",
                "38 - Glenway Crossing - Uptown - Outbound",
                "40 - Montana Avenue Express - Inbound",
                "40 - Montana Avenue Express - Outbound",
                "41 - Crosstown: Westwood - Oakley - Eastbound",
                "41 - Crosstown: Westwood - Oakley - Westbound",
                "42 - West Chester Express - Inbound",
                "42 - West Chester Express - Outbound",
                "43 - Reading Rd-Winton Hills-Bond Hill - Inbound",
                "43 - Reading Rd-Winton Hills-Bond Hill - Outbound",
                "46 - Avondale - Corryville - Zoo - Inbound",
                "46 - Avondale - Corryville - Zoo - Outbound",
                "49 - Queensgate - Fairmount - Inbound",
                "49 - Queensgate - Fairmount - Outbound",
                "50 - River Road - Saylor Park - Inbound",
                "50 - River Road - Saylor Park - Outbound",
                "51 - Glenway Crossing - Hyde Park Oakley - Westbound",
                "51 - Glenway Crossing - Hyde Park Oakley - Eastbound",
                "52 - Harrison Express - Inbound",
                "52 - Harrison Express - Outbound",
                "64 - Glenway Crossing Westwood McMicken - Inbound",
                "64 - Glenway Crossing Westwood McMicken - Outbound",
                "67 - Sharonville Blue Ash Job Connection - Inbound",
                "67 - Sharonville Blue Ash Job Connection - Outbound",
                "71 - Warren County Express Kings Island - Inbound",
                "71 - Warren County Express Kings Island - Outbound",
                "72 - Kings Island Direct - Inbound",
                "72 - Kings Island Direct - Outbound",
                "74 - Colerain Express - Inbound",
                "74 - Colerain Express - Outbound",
                "75 - Anderson Express - Inbound",
                "75 - Anderson Express - Outbound",
                "77 - Delhi Express - Glenway Crossing - Inbound",
                "77 - Delhi Express - Glenway Crossing - Outbound",
                "78 - Tri - County - Lincoln Heights - Inbound",
                "78 - Tri - County - Lincoln Heights - Outbound",
                "81 - Mt. Washington Express - Inbound",
                "81 - Mt. Washington Express - Outbound",
                "82 - Eastgate Express - Inbound",
                "82 - Eastgate Express - Outbound",
                "85 - Riverfront Parking Shuttle - AM Inbound",
                "85 - Riverfront Parking Shuttle - PM Outbound",
                "90 - MetroPlus - Kenwood - Inbound",
                "90 - MetroPlus - Kenwood - Outbound"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        routeList.setAdapter(adapter);

        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item value
                String itemValue = (String) routeList.getItemAtPosition(position);

                //Loading next activity
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra(ROUTE, itemValue);
                MainActivity.this.startActivity(intent);
            }

        });
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

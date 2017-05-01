package com.lobotrock.cincystreetcarmapper;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * MapActivity injects the route selecting javascript,
 * removes the left menu bar and displays the map from the bus tracker website.
 *
 * Created by Drew Albrecht on 5/1/17.
 */
public class MapActivity extends AppCompatActivity {
    protected ProgressDialog progressDialog;
    protected WebView webView;

    //The selected route to display
    private String selectedRoute;

    //Constants
    //The url loaded for the bus tracking
    final private String BUS_TRACKER_URL = "http://bustracker.go-metro.com/hiwire?.a=iRealTimeDisplay";
    //Used to load the script after a set timeout
    final private String SCRIPT_LOADER = "javascript:setTimeout(showRoute(), 100)";
    //The message the console logs out from js/script.js to indicate the javascript finished loading
    final private String FINISH_LOADING_INDICATOR = "finished android display";
    //Placeholder in the javascript that needs to be replaced with the route information
    final private String ROUTE_PLACEHOLDER = "<selectedRoute>";
    //Text displayed when loading
    final private String LOADING_TEXT = "Loading...";

    /**
     * This will load the Cincinnati Real-Time Bus Tracker site, select the Streetcar inbound and
     *  out bound routes, and hide the left navigation.
     */
    private void loadStreetCarView(){
        webView = (WebView)findViewById(R.id.webView);

        webView.setKeepScreenOn(true);
        webView.setInitialScale(180);   //Scale is larger than 100 due to the left controls removed
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setBackgroundColor(Color.BLACK);


        final WebSettings webSettings = webView.getSettings();
        //Enabling javascript is needed, since I'm injecting my own javascript to the metro site.
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                injectScriptFile(view, "js/script.js");

                // Running injected script
                view.loadUrl(SCRIPT_LOADER);
            }
        });

        //WebChromeClient is responsible for the progress dialog
        webView.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && progressDialog == null){
                    progressDialog = new ProgressDialog(MapActivity.this);
                    progressDialog.setMessage(LOADING_TEXT);
                    progressDialog.show();
                }
                if(progressDialog != null) {
                    progressDialog.setMessage(LOADING_TEXT + progress + "% ");
                }
            }

            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d(this.getClass().getName(), consoleMessage.message());
                if(consoleMessage.message().contains(FINISH_LOADING_INDICATOR)){
                    if(progressDialog != null){
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });

        //Call to the page
        webView.loadUrl(BUS_TRACKER_URL);
    }

    /**
     * Injecting the script here will remove toolsContentLeft, remove the margins, select
     *  the street car routes.  To see the details go to app/src/main/assets/js/script.js
     *
     * @param view The webview having script injected into
     * @param scriptFile The script to inject.
     */
    private void injectScriptFile(WebView view, String scriptFile) {
        String script = readFiletoString(scriptFile);
        script = script.replaceAll(ROUTE_PLACEHOLDER, this.selectedRoute);

        // String-ify the script byte-array using BASE64 encoding
        String encoded = Base64.encodeToString(script.getBytes(), Base64.NO_WRAP);
        view.loadUrl("javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var script = document.createElement('script');" +
                "script.type = 'text/javascript';" +
                // Tell the browser to BASE64-decode the string into your script
                "script.innerHTML = window.atob('" + encoded + "');" +
                "parent.appendChild(script)" +
                "})()");

    }

    private String readFiletoString(String file){
        InputStream inputStream;
        StringBuilder stringBuilder = new StringBuilder();

        try{
            inputStream = getAssets().open(file);
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String string;

            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string);
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            this.selectedRoute = extras.getString(MainActivity.ROUTE);
            Log.d(this.getClass().getName(), "Calling onCreate for Map Activity for " + this.selectedRoute);
            setContentView(R.layout.activity_map);

            loadStreetCarView();
        }
    }

}

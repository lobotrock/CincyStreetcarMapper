package com.lobotrock.cincystreetcarmapper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.app.ProgressDialog;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    //protected ProgressBar progressBar;
    protected ProgressDialog progressDialog;
    protected WebView webView;

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
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                injectScriptFile(view, "js/script.js");

                // Running injected script
                view.loadUrl("javascript:setTimeout(showStreetcar(), 100)");
            }

            /**
             * Injecting the script here will remove toolsContentLeft, remove the margins, select
             *  the street car routes.  To see the details go to app/src/main/assets/js/script.js
             *
             * @param view The webview having script injected into
             * @param scriptFile The script to inject.
             */
            private void injectScriptFile(WebView view, String scriptFile) {
                InputStream input;
                try {
                    input = getAssets().open(scriptFile);
                    byte[] buffer = new byte[input.available()];
                    input.read(buffer);
                    input.close();


                    // String-ify the script byte-array using BASE64 encoding !!!
                    String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                    view.loadUrl("javascript:(function() {" +
                            "var parent = document.getElementsByTagName('head').item(0);" +
                            "var script = document.createElement('script');" +
                            "script.type = 'text/javascript';" +
                            // Tell the browser to BASE64-decode the string into your script !!!
                            "script.innerHTML = window.atob('" + encoded + "');" +
                            "parent.appendChild(script)" +
                            "})()");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //WebChromeClient is responsible for the progress dialog
        webView.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && progressDialog == null){
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
                if(progressDialog != null) {
                    progressDialog.setMessage("Loading... " + progress + "% ");
                }
            }

            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                System.out.println(consoleMessage.message());
                if(consoleMessage.message().contains("finished android display")){
                    if(progressDialog != null){
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });

        //Call to the page
        webView.loadUrl("http://bustracker.go-metro.com/hiwire?.a=iRealTimeDisplay");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Calling onCreate");
        setContentView(R.layout.activity_main);

        if(haveNetworkConnection()){
            loadStreetCarView();
        }
        else{
            AlertDialog errorDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Error")
                    .setMessage("No Network Connection Found!")
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
                    .show();
        }

    }
}

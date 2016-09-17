package com.lobotrock.cincystreetcarmapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
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

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                if (netInfo.isConnected()) {
                    haveConnectedWifi = true;
                }
            }
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (netInfo.isConnected()) {
                    haveConnectedMobile = true;
                }
            }

        return haveConnectedWifi || haveConnectedMobile;
    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(progressDialog == null){
            initProgressDialog();
        }

        webView = (WebView)findViewById(R.id.webView);

        //TODO: Get scrolling and zooming working on map
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                injectScriptFile(view, "js/script.js");

                // Running injected script
                view.loadUrl("javascript:setTimeout(showStreetcar(), 100)");
            }

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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });





        webView.setWebChromeClient(new WebChromeClient(){



            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // When user clicks a hyperlink, load in the existing WebView

                view.loadUrl(url);
                return true;
            }

            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && progressDialog == null){
                    initProgressDialog();
                }
                if(progressDialog != null) {
                    progressDialog.setMessage("Loading... " + progress + "% ");
                }
                if(progress == 100 && progressDialog != null){
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });
        
        webView.loadUrl("http://bustracker.go-metro.com/hiwire?.a=iRealTimeDisplay");
    }
}

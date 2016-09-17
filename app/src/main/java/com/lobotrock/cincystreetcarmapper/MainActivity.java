package com.lobotrock.cincystreetcarmapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    protected ProgressBar progressBar;
    protected WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        webView = (WebView)findViewById(R.id.webView);
        webView.setVisibility(View.GONE);

        //TODO: Get scrolling and zooming working on map
        webView.getSettings().setJavaScriptEnabled(true);

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
            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    //TODO: Get progressbar to show progress
                    progressBar.setProgress(progress);
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    //webView.setVisibility(WebView.GONE);
                }
                progressBar.setProgress(progress);
                if(progress == 100 && webView.getVisibility() == View.GONE) {
                    progressBar.setVisibility(ProgressBar.GONE);
                    //TODO: Get webView to be focused
                    webView.setVisibility(View.VISIBLE);

                }
            }
        });
        
        webView.loadUrl("http://bustracker.go-metro.com/hiwire?.a=iRealTimeDisplay");
    }
}

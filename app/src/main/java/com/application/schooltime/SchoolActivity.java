package com.application.schooltime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.application.schooltime.SchoolInformation.SchoolInformationActivity;
import com.application.schooltime.Utilities.PrefManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SchoolActivity extends AppCompatActivity {

    private  String PAGE_URL;
    private WebView webView;
    LinearLayout layout_error  ;
    Button  btn_retry ;
    ProgressBar progress ;

    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_times);

        //
        PAGE_URL= new PrefManager(this).getSchoolUrl();

        layout_error = findViewById(R.id.error_layout);
        btn_retry = findViewById(R.id.btn_retry);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        progress = findViewById(R.id.progress);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadWebViewUrl(PAGE_URL);
            }
        });
        CookieSyncManager.createInstance(this);

        initWebView();


        //******************* SETTING UP THE ALERT DIALOG ********************************////////
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        alertDialog.setMessage("This option will allow you to change your current school.\nDo you wish to proceed? ");
        alertDialog.setIcon(R.drawable.alerticon);
        alertDialog.setTitle("Change Schools");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new PrefManager(getApplicationContext()).setSchoolUrl(null);
                startSchoolInformationActivity();
                finish();

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = alertDialog.create();


        ///////////////////*************** FLOATING ACTION BUTTON TO CHANGE THE SCHOOL **************/////////////////
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.show();

            }
        });

    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress.setVisibility(View.VISIBLE);

        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
            layout_error.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
        }
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //Toast.makeText(SchoolActivity.this,"Something Wrong",Toast.LENGTH_SHORT).show();
            webView.setVisibility(View.VISIBLE);
            layout_error.setVisibility(View.GONE);
        }
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

            //Toast.makeText(SchoolActivity.this,"Something Wrong",Toast.LENGTH_SHORT).show();
            webView.setVisibility(View.VISIBLE);
            layout_error.setVisibility(View.GONE);
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);

        }



    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            isCanGoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webView = (WebView) findViewById(R.id.web);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        LoadWebViewUrl(PAGE_URL);
    }

    private void LoadWebViewUrl(String url) {
        if (isInternetConnected()) {
            webView.setVisibility(View.VISIBLE);
            layout_error.setVisibility(View.GONE);
            webView.loadUrl(url);
        } else {
            webView.setVisibility(View.GONE);
            layout_error.setVisibility(View.VISIBLE);
        }
    }
    private void isCanGoBack() {
        if (webView.canGoBack())
            webView.goBack();
        else
            finish();
    }
    public boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }
    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    void startSchoolInformationActivity(){
        startActivity(new Intent(SchoolActivity.this, SchoolInformationActivity.class));
        finish();
    }
}

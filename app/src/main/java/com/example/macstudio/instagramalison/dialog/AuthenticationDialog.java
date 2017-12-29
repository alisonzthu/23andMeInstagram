package com.example.macstudio.instagramalison.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.ApplicationConsts;
import com.example.macstudio.instagramalison.listener.AuthenticationListener;

import static android.content.ContentValues.TAG;

/**
 * Created by macstudio on 12/18/17.
 */

public class AuthenticationDialog extends Dialog {

    private AuthenticationListener listener;
    private Context context;
//    private WebView webView;

    private final String url = ApplicationConsts.BASE_URL
            + "oauth/authorize/?client_id=" + ApplicationConsts.CLIENT_ID
            + "&redirect_uri=" + ApplicationConsts.REDIRECT_URI
            + "&response_type=code" + "&scope=public_content";

    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);
        initializeWebView();
    }

    private void initializeWebView() {

//        webView = findViewById(R.id.web_view);
//        webView.loadUrl(url);
//        webView.setWebViewClient(new WebViewClient(){
//            boolean authComplete = false;
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.d(TAG, "Redirecting URL " + url);
//
//                if(url.startsWith(ApplicationConsts.REDIRECT_URI)) {
//                    String urls[] = url.split("=");
//                    listener.onComplete(urls[1]);
//                    return true;
//                }
//                return false;
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon){
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
//            }
//
//        });

    }
}

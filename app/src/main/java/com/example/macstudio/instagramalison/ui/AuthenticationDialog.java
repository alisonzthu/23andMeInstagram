package com.example.macstudio.instagramalison.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.AppConstants;
import com.example.macstudio.instagramalison.api.services.SharedPrefManager;
import com.example.macstudio.instagramalison.listener.AuthenticationListener;


/**
 * Created by macstudio on 12/18/17.
 */

public class AuthenticationDialog extends Dialog {

    private static final String TAG = AuthenticationDialog.class.getSimpleName();
    private AuthenticationListener authListener;
    private WebView webView;

    private final String url = AppConstants.BASE_URL
            + "oauth/authorize/?client_id=" + AppConstants.CLIENT_ID
            + "&redirect_uri=" + AppConstants.REDIRECT_URI
            + "&response_type=token&scope=public_content+likes";


    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        authListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);
        setUpWebView();
    }

    private void setUpWebView() {
        webView = findViewById(R.id.web_view);

        webView.setWebViewClient(new AuthWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        webView.loadUrl(url);

    }

    private class AuthWebViewClient extends WebViewClient {
        boolean authComplete = false;
        String access_token = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.i(TAG, "page started, url: " + url);
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest request, WebResourceError error) {
            Log.e(TAG, "WebViewClient onReceived Error: " + error.toString());
            dismiss();
            // detect if auth_token is there
            SharedPreferences sharedPreferences= getContext().getSharedPreferences(SharedPrefManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            access_token = sharedPreferences.getString(SharedPrefManager.KEY_ACCESS_TOKEN, "");
            if (access_token != "") {
                // this case the user can see feedActivity with DB DATA
                // access_token is still there so the user is still logged in, should allow the user
                // to just see her feeds
                authListener.onNoInternet();
            } else {
                // user has logged out, can't show any feeds
                Toast.makeText(getContext(), "Can't show any feed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e(TAG, "WebViewClient errored: " + description);
            Toast.makeText(getContext(), "error 2", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i(TAG, "WebView page finished, url: " + url);
            if(url.contains("#access_token=") && !authComplete) {
                Uri uri = Uri.parse(url);
                access_token = uri.getEncodedFragment();
                access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
                Log.i(TAG, "Got access_token");
                authComplete = true;
                authListener.onTokenReceived(access_token);
                dismiss();
            }
        }
    }
}

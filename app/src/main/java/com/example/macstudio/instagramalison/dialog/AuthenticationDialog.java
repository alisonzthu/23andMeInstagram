package com.example.macstudio.instagramalison.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.ApplicationConsts;
import com.example.macstudio.instagramalison.listener.AuthenticationListener;

import static android.content.ContentValues.TAG;

/**
 * Created by macstudio on 12/18/17.
 */

public class AuthenticationDialog extends Dialog {

    private static final String TAG = AuthenticationDialog.class.getSimpleName();
    private AuthenticationListener authListener;
    private WebView webView;
    private Context context;

    private final String url = ApplicationConsts.BASE_URL
            + "oauth/authorize/?client_id=" + ApplicationConsts.CLIENT_ID
            + "&redirect_uri=" + ApplicationConsts.REDIRECT_URI
            + "&response_type=token&scope=public_content+likes";


    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        this.context = context;
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
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearView();
        webView.loadUrl(url);
//        webView.reload();
        webView.setWebViewClient(new AuthWebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
    }

    private class AuthWebViewClient extends WebViewClient {
        boolean authComplete = false;
        String access_token;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "page started, url: " + url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d("onReceivedError", "errored");
            Log.d("Error message", description);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            spinner.dismiss(); // keep or not keep the spinner feature?
            Log.d(TAG, "page finished, url: " + url);
            if(url.contains("#access_token=") && !authComplete) {
                Uri uri = Uri.parse(url);
                access_token = uri.getEncodedFragment();
                access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
                Log.i("", "CODE: " + access_token);
                authComplete = true;
                authListener.onTokenReceived(access_token);
                dismiss();
            } else if (url.contains("?error")) {
                Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }
}

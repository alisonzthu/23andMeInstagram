package com.example.macstudio.instagramalison.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.dialog.AuthenticationDialog;
import com.example.macstudio.instagramalison.listener.AuthenticationListener;

public class MainActivity extends AppCompatActivity implements AuthenticationListener{
    private AuthenticationDialog auth_dialog;
//    private Button btn_get_access_token;
    private String mAuthToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        btn_get_access_token = findViewById(R.id.btn_get_access_token);
        auth_dialog = new AuthenticationDialog(MainActivity.this, this);
        auth_dialog.show();
    }

    @Override
    public void onCodeReceived(String code) {
        if (code != null) {
            auth_dialog.dismiss();
        }
    }
}

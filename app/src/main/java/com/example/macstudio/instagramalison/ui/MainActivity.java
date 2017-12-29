package com.example.macstudio.instagramalison.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.ApplicationConsts;
import com.example.macstudio.instagramalison.dialog.AuthenticationDialog;
import com.example.macstudio.instagramalison.listener.AuthenticationListener;

public class MainActivity extends AppCompatActivity implements AuthenticationListener{
    private AuthenticationDialog auth_dialog;
    private Button btn_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_connect = findViewById(R.id.btn_connect);

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth_dialog = new AuthenticationDialog(MainActivity.this, MainActivity.this);
                auth_dialog.setCancelable(true);
                auth_dialog.show();
            }
        });
    }

    @Override
    public void onComplete(String code) {
        // if code is not null, need to get access_token
        if (code != null) {
            Toast.makeText(MainActivity.this, "code: " + code, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, "Error message: " + error, Toast.LENGTH_SHORT);
    }
}

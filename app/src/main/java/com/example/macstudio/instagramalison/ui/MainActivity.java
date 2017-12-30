package com.example.macstudio.instagramalison.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.ApplicationConsts;
import com.example.macstudio.instagramalison.api.model.TokenResponse;
import com.example.macstudio.instagramalison.api.services.ServiceGenerator;
import com.example.macstudio.instagramalison.dialog.AuthenticationDialog;
import com.example.macstudio.instagramalison.listener.AuthenticationListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AuthenticationListener{
    private AuthenticationDialog auth_dialog;
    private Button btn_connect;
    private TextView failureText;
    private String authToken;

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
    public void onTokenReceived(String access_token) {
        if (access_token != null) {
            Toast.makeText(MainActivity.this, "hahaha", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "nonono", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, "Error message: " + error, Toast.LENGTH_SHORT).show();
    }
}

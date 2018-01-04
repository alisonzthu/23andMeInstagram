package com.example.macstudio.instagramalison.ui;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.dialog.AuthenticationDialog;
import com.example.macstudio.instagramalison.listener.AuthenticationListener;

public class MainActivity extends AppCompatActivity implements AuthenticationListener{
    private AuthenticationDialog auth_dialog;
    private Button btn_connect;
    private String access_token = "";

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

//        Window window = this.getWindow();
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

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
            this.access_token = access_token;
            Intent feedIntent = new Intent(MainActivity.this, FeedActivity.class);
            feedIntent.putExtra("access_token", access_token);
            startActivity(feedIntent);
        } else {
            auth_dialog.dismiss();
            Toast.makeText(MainActivity.this, "nonono", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, "Error message: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_LONG).show();

                if (this.access_token != "") {
                    // this should be how to log user out
                    this.access_token = "";
                    // this won't work for now because I can't get the access_token from api
                    item.setVisible(false);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}

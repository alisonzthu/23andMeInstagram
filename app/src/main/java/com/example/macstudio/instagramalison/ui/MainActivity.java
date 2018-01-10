package com.example.macstudio.instagramalison.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.model.InstaUserResponse;
import com.example.macstudio.instagramalison.api.services.ServiceGenerator;
import com.example.macstudio.instagramalison.api.services.SharedPrefManager;
import com.example.macstudio.instagramalison.listener.AuthenticationListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AuthenticationListener{
    private AuthenticationDialog auth_dialog;
    private Button btn_connect;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, SharedPrefManager.getInstance(this).isLoggedIn() + "");

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
// to change the statusbar color:
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
    public void onDestroy() {
        super.onDestroy();
        // destroy info stored in SharedPreferences:
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Log.d(TAG, "user is still logged in");

            try{
                SharedPrefManager.getInstance(this).logout();
            } catch(Exception e) {
                Log.e(TAG, "Fail to log user out");
            }
        }
    }

    @Override
    public void onTokenReceived(String access_token) {
        if (access_token != null) {
            Log.i(TAG, "Received access_token");
            final String ACCESS_TOKEN = access_token;
            SharedPrefManager.getInstance(getApplicationContext())
                    .userLogin(ACCESS_TOKEN);
            Intent feedIntent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(feedIntent);
        } else {
            auth_dialog.dismiss();
            Log.wtf(TAG, "access_token is null");
            Toast.makeText(MainActivity.this, "Can NOT access API", Toast.LENGTH_LONG).show();
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
            case R.id.action_logout:
                Toast.makeText(MainActivity.this, "action_logout", Toast.LENGTH_LONG).show();
                if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                    // this should be how to log user out
                    SharedPrefManager.getInstance(this).logout();
                    // this won't work for now, because I can't get the access_token from api
                    // this link may help: https://stackoverflow.com/questions/42890528/how-to-hide-menu-item-in-android-action-bar
                    item.setVisible(false);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}

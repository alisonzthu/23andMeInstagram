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

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("from onCreate", SharedPrefManager.getInstance(this).isLoggedIn() + "");

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
    public void onDestroy() {

        super.onDestroy();
        // destroy sharedpreferences:
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Log.d("user is logged in", SharedPrefManager.getInstance(this).isLoggedIn() + "");
            // this should be how to log user out
            SharedPrefManager.getInstance(this).logout();
            Log.d("user is logged in", SharedPrefManager.getInstance(this).isLoggedIn() + "");
        }

    }

    @Override
    public void onTokenReceived(String access_token) {
        if (access_token != null) {
            final String ACCESS_TOKEN = access_token;
            Toast.makeText(MainActivity.this, "hahaha", Toast.LENGTH_LONG).show();

            Call<InstaUserResponse> call = ServiceGenerator.createUserDataService().getUserProfile(access_token);
            call.enqueue(new Callback<InstaUserResponse>() {
                @Override
                public void onResponse(Call<InstaUserResponse> call, Response<InstaUserResponse> response) {

                    if (response.body() != null && response.body().getData() != null) {
                        Log.d("not null case", response.body().getData().getId());
                        // put both access_token and user_id into SharedPreferences
                        SharedPrefManager.getInstance(getApplicationContext())
                                .userLogin(ACCESS_TOKEN, response.body().getData().getId());
                    }
                }

                @Override
                public void onFailure(Call<InstaUserResponse> call, Throwable t) {
                    Log.e("Erred", "in getting user data");
                }
            });
            Intent feedIntent = new Intent(MainActivity.this, FeedActivity.class);
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

package com.example.macstudio.instagramalison.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.model.InstagramData;
import com.example.macstudio.instagramalison.api.model.InstagramResponse;
import com.example.macstudio.instagramalison.api.services.ServiceGenerator;
import com.example.macstudio.instagramalison.api.services.SharedPrefManager;
import com.example.macstudio.instagramalison.ui.adapter.SimpleListViewAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {

    private String access_token = null;
    private ListView feedListView;
    private static final String TAG = "FeedActivity";

    private SimpleListViewAdapter listViewAdapter;
    private ArrayList<InstagramData> feedData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences(SharedPrefManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        access_token = sharedPreferences.getString(SharedPrefManager.KEY_ACCESS_TOKEN, "");
        System.out.print("access_token is " + access_token);

        feedListView = findViewById(R.id.feed_list);

        listViewAdapter = new SimpleListViewAdapter(this, 0, feedData);
        feedListView.setAdapter(listViewAdapter);
        fetchFeedData();
    }

    public void fetchFeedData() {
        Call<InstagramResponse> call = ServiceGenerator.createGetFeedService().getOwnPhotos(access_token);
        call.enqueue(new Callback<InstagramResponse>() {

            @Override
            public void onResponse(Call<InstagramResponse> call, Response<InstagramResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "Received none null Instagram response data");
                    for (int i = 0; i < response.body().getData().length; i++) {
                        feedData.add(response.body().getData()[i]);
                    }

                    if (feedData.size() == 0) {
                        Toast.makeText(FeedActivity.this, "You don't have any feed", Toast.LENGTH_SHORT).show();
                        // would be better to show the message on screen
                    }

                    listViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InstagramResponse> call, Throwable t) {
                Log.e(TAG, "Error getting Instagram response: " + t.getMessage());
            }
        });
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
                Toast.makeText(FeedActivity.this, "action_logout", Toast.LENGTH_LONG).show();

                if (this.access_token != null) {
                    this.access_token = null;
                    item.setVisible(false);
                    // send the user back to MainActivity:
                    SharedPrefManager.getInstance(this).logout();
                    finish();
                    startActivity(new Intent(this, MainActivity.class));
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

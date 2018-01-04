package com.example.macstudio.instagramalison.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.model.InstagramData;
import com.example.macstudio.instagramalison.api.model.InstagramResponse;
import com.example.macstudio.instagramalison.api.services.ServiceGenerator;
import com.example.macstudio.instagramalison.ui.adapter.SimpleListViewAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {

    private String access_token = "";
    private ListView feedListView;

    private SimpleListViewAdapter listViewAdapter;
    private ArrayList<InstagramData> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Intent intent = getIntent();
        access_token =  intent.getStringExtra("access_token");

        feedListView = findViewById(R.id.feed_list);

        listViewAdapter = new SimpleListViewAdapter(this, 0, data);
        feedListView.setAdapter(listViewAdapter);
        fetchData();
    }

    public void fetchData() {
        Call<InstagramResponse> call = ServiceGenerator.createGetFeedService().getOwnPhotos(access_token);
        call.enqueue(new Callback<InstagramResponse>() {

            @Override
            public void onResponse(Call<InstagramResponse> call, Response<InstagramResponse> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().getData().length; i++) {
                        data.add(response.body().getData()[i]);
                    }

                    listViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InstagramResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
        // when there's no data??

        // do I need to call clearListView() anywhere?
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
                //your action
                Toast.makeText(FeedActivity.this, "action_settings", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}

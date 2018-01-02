package com.example.macstudio.instagramalison.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.model.InstagramData;
import com.example.macstudio.instagramalison.ui.adapter.SimpleListViewAdapter;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private String access_token;
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
    }
}

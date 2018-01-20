package com.example.macstudio.instagramalison.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.model.InstagramData;
import com.example.macstudio.instagramalison.api.model.InstagramResponse;
import com.example.macstudio.instagramalison.api.services.ServiceGenerator;
import com.example.macstudio.instagramalison.api.services.SharedPrefManager;
import com.example.macstudio.instagramalison.database.FeedContract;
import com.example.macstudio.instagramalison.database.FeedCursorAdapter;
import com.example.macstudio.instagramalison.database.FeedDbHelper;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {

    private String access_token = null;
    private static final String TAG = FeedActivity.class.getSimpleName();
    private FeedDbHelper feedDbHelper = new FeedDbHelper(this);
    private ArrayList<InstagramData> feedData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // move intent to top & when intent == null show something
        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(FeedActivity.this, "Oh, my!", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences(SharedPrefManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        access_token = sharedPreferences.getString(SharedPrefManager.KEY_ACCESS_TOKEN, "");
        Log.i(TAG, "Obtained access_token");

        SQLiteDatabase db = feedDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM feeds", null);
        final FeedCursorAdapter feedCursorAdapter = new FeedCursorAdapter(this, cursor);
        cursor.close(); // close cursor after usage!!! immediately!!!!
        ListView feedListView = findViewById(R.id.feed_list);
        feedListView.setAdapter(feedCursorAdapter);

        // only fetch and save data when there's internet connection
        // what if the screen turns into landscape view, is it getting that booleanExtra???
        if (intent.getBooleanExtra("Internet", false)) {
            fetchAndSaveFeedData(feedCursorAdapter);
        } else if (!intent.getBooleanExtra("Internet", false)) {
            Toast.makeText(getApplicationContext(), "Read ONLY", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchAndSaveFeedData(final FeedCursorAdapter adapter) {
        Call<InstagramResponse> call = ServiceGenerator.createGetFeedService().getOwnPhotos(access_token);
        call.enqueue(new Callback<InstagramResponse>() {

            @Override
            public void onResponse(@NonNull Call<InstagramResponse> call, @NonNull Response<InstagramResponse> response) {
                if (response.isSuccessful()) {
                    InstagramResponse responseBody = response.body();
                    if (responseBody != null) {
                        Log.d(TAG, "Received none null Instagram response data");

                        if (responseBody.getData().length == 0) {
                            Toast.makeText(FeedActivity.this, "You don't have any feed", Toast.LENGTH_SHORT).show();
                            // would be better to show the message on screen
                        } else {
                            feedData.addAll(Arrays.asList(responseBody.getData()));
                            saveDataToDb(feedData);
                        }
                        SQLiteDatabase db = feedDbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("SELECT * FROM feeds", null);
                        adapter.changeCursor(cursor);
                    }
                } else {
                    // response is not successful (such as 40x case):
                    Log.w(TAG, "Response not successful");
                    Toast.makeText(FeedActivity.this, "Response failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstagramResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error getting Instagram response: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Fail to fetch data", Toast.LENGTH_SHORT).show();
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

    private void saveDataToDb(ArrayList<InstagramData> data) {
        SQLiteDatabase db = feedDbHelper.getWritableDatabase();
        db.delete(FeedContract.FeedEntry.TABLE_NAME, null, null);

        for (InstagramData dataPoint: data) {
            ContentValues values = new ContentValues();
            values.put(FeedContract.FeedEntry.COLUMN_NAME_USERNAME, dataPoint.getUser().getFull_name());
            values.put(FeedContract.FeedEntry.COLUMN_NAME_AVATAR, dataPoint.getUser().getProfile_picture());
            values.put(FeedContract.FeedEntry.COLUMN_NAME_IMAGE, dataPoint.getImages().getStandard_resolution().getUrl());
            values.put(FeedContract.FeedEntry.COLUMN_NAME_LIKESTATUS, dataPoint.isUser_has_liked() ? 1 : 0);
            values.put(FeedContract.FeedEntry.COLUMN_NAME_LIKECOUNT, dataPoint.getLikes().getCount());
            values.put(FeedContract.FeedEntry.COLUMN_NAME_MEDIAID, dataPoint.getId());

            db.insert(FeedContract.FeedEntry.TABLE_NAME, null, values);
        }
    }
}

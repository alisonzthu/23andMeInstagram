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
    public void onPause() {
        super.onPause();
        if (auth_dialog.isShowing()) {
            auth_dialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (auth_dialog.isShowing()) {
            auth_dialog.dismiss();
        }
    }

    @Override
    public void onCodeReceived(String code) {
        if (code != null) {
            Log.d("received codesss: ", code);
            auth_dialog.dismiss();
            // POST request to get access_token
            final Call<TokenResponse> accessToken = ServiceGenerator
                    .createTokenService()
                    .getAccessToken(ApplicationConsts.CLIENT_ID, ApplicationConsts.CLIENT_SECRET, ApplicationConsts.REDIRECT_URI,
                            ApplicationConsts.GRANT_TYPE, code);
            accessToken.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    Log.d("onResponse: ", "has response");
                    Log.d("response code: ", response.code()+ "");
                    if(response.isSuccessful()) {
                        Log.d("success response", "yeah!");
                        Toast.makeText(MainActivity.this, "successful!", Toast.LENGTH_SHORT).show();
                        authToken = response.body().getAccess_token();
                        Toast.makeText(MainActivity.this, "authToken: " + authToken, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("??????", "what?");
                    }
//                    if (response.body() != null) {
//                        authToken = response.body().getAccess_token();
//                        Log.d("body is not null: ", authToken);
//                    } else {
//                        Log.d("body is null", " so sad");
//                    }
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    Log.d("fail response", "nooooo!");
                    Toast.makeText(MainActivity.this, "Fail to get token response!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, "Error message: " + error, Toast.LENGTH_SHORT).show();
    }
}

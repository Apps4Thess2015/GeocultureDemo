package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.NetworkUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private View bCurrentLocation, bPickLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bCurrentLocation = findViewById(R.id.bCurrentLocation);
        bPickLocation = findViewById(R.id.bPickLocation);

        bCurrentLocation.setOnClickListener(this);
        bPickLocation.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCurrentLocation:
                if (!new NetworkUtils(HomeActivity.this).hasInternet())
                    Snackbar.make(bCurrentLocation, "No internet connection", Snackbar.LENGTH_SHORT).show();
                else
                    Snackbar.make(bCurrentLocation, "Current Location", Snackbar.LENGTH_SHORT).show();

                break;
            case R.id.bPickLocation:
                if (!new NetworkUtils(HomeActivity.this).hasInternet())
                    Snackbar.make(bCurrentLocation, "No internet connection", Snackbar.LENGTH_SHORT).show();
                else
                    Snackbar.make(bPickLocation, "Pick Location", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}

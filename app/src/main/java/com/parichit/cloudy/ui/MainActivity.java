package com.parichit.cloudy.ui;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parichit.cloudy.R;
import com.parichit.cloudy.data.models.ForecastResponse;
import com.parichit.cloudy.data.models.Result;
import com.parichit.cloudy.utils.WeatherUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {
    static final int MY_PERMISSIONS_LOCATION = 616;
    private static final String TAG = "MainActivity";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private MainViewModel viewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    private String loc;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ForecastsAdapter adapter;
    private TextView currentTemp;
    private TextView currentCity;
    private ConstraintLayout loadingLayout, errorLayout;
    private LinearLayout forecastLayout;
    private Button retry;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        loadingLayout = findViewById(R.id.loading_layout);
        errorLayout = findViewById(R.id.error_layout);
        forecastLayout = findViewById(R.id.forecast_layout);
        retry = findViewById(R.id.retry_button);
        currentCity = findViewById(R.id.city_name);
        currentTemp = findViewById(R.id.current_temp);
        recyclerView = findViewById(R.id.forecast_list);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.refreshForecasts(loc);
            }
        });

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        LayoutAnimationController animation =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_slide_up);
        recyclerView.setLayoutAnimation(animation);

        adapter = new ForecastsAdapter(this);
        recyclerView.setAdapter(adapter);

    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATION);
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    Log.d(TAG, addresses.get(0).getLocality());
                                    loc = addresses.get(0).getLocality();
                                    initialize(loc);
//                                    Log.d(TAG, addresses.get(0).getPostalCode());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
//                                loc = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
//                                Toast.makeText(getApplicationContext(), "Location: " + coordinates, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATION: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getCurrentLocation();
                else
                    Toast.makeText(this, "Location permission must be provided", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void initialize(String coordinates) {
        viewModel.init(coordinates);
        viewModel.getForecasts().observe(this, new Observer<Result>() {
            @Override
            public void onChanged(@Nullable Result result) {
                Log.d(TAG, "Status: " + result.status);
                if(result != null) {
//                    Log.d(TAG, "Size: " + forecastResponse.getForecast().getForecastday().size());
                    processResult(result);
                }
            }
        });
    }

    void processResult(Result result) {
        switch (result.status) {
            case LOADING:
                showLoading();
                break;
            case ERROR:
                showError();
                break;
            case SUCCESS:
                showForecasts(result.data);
                break;
        }
    }

    void showForecasts(ForecastResponse forecastResponse) {
        currentTemp.setText(WeatherUtil.toDegrees(forecastResponse.getCurrent().getTemperature()));
        currentCity.setText(loc);
        adapter.setForecasts(forecastResponse.getForecast().getForecastday());
        adapter.notifyDataSetChanged();

        loadingLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        forecastLayout.setVisibility(View.VISIBLE);
        recyclerView.scheduleLayoutAnimation();
    }

    void showLoading() {
        if(loadingLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.GONE);
            forecastLayout.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.VISIBLE);
        }
    }

    void showError() {
        loadingLayout.setVisibility(View.GONE);
        forecastLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }
}

package com.parichit.cloudy.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.parichit.cloudy.data.WeatherRepository;
import com.parichit.cloudy.data.models.Result;

import javax.inject.Inject;

public class MainViewModel extends ViewModel{
    private static final String TAG = "MainViewModel";
    private LiveData<Result> forecasts;
    private WeatherRepository weatherRepository;
    private String loc;

    @Inject
    public MainViewModel(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public void init(String loc) {
        this.loc = loc;
        Log.d(TAG, String.valueOf(loc));
        refreshForecasts(loc);
    }

    public LiveData<Result> getForecasts() {
//        if(forecasts == null)
//            forecasts = weatherRepository.getWeatherForecasts(loc, 7);
        return forecasts;
    }

    public void refreshForecasts(String loc) {
        forecasts = weatherRepository.getWeatherForecasts(loc, 7);
    }
}

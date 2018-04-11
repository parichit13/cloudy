package com.parichit.cloudy.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.parichit.cloudy.data.models.ForecastResponse;
import com.parichit.cloudy.data.models.Result;
import com.parichit.cloudy.data.network.RetrofitService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherRepository {
    private static final String TAG = "Repository";
    private RetrofitService retrofitService;
    private MutableLiveData<Result> data = new MutableLiveData<>();

    @Inject
    public WeatherRepository(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    public LiveData<Result> getWeatherForecasts(String query, int days) {
//        final MutableLiveData<Result> data = new MutableLiveData<>();
        data.setValue(Result.loading());
        retrofitService.getForecast(query, days).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
//                Log.d(TAG, call.request().url().toString());
                if(response.isSuccessful()) {
                    Log.d(TAG, "Success");
//                    data.postValue(response.body());
                    data.postValue(Result.success(response.body()));
                } else {
                    data.postValue(Result.error(new Throwable("Not Sucessful")));
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                data.postValue(Result.error(t));
            }
        });

        return data;
    }


}

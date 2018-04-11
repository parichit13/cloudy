package com.parichit.cloudy.data.network;

import com.parichit.cloudy.data.models.ForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("forecast.json")
    Call<ForecastResponse> getForecast(@Query("q") String query, @Query("days") Integer days );
}

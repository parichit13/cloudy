package com.parichit.cloudy.data.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.parichit.cloudy.data.models.Status.ERROR;
import static com.parichit.cloudy.data.models.Status.LOADING;
import static com.parichit.cloudy.data.models.Status.SUCCESS;

public class Result {

    public final Status status;

    @Nullable
    public final ForecastResponse data;

    @Nullable
    public final Throwable error;

    public Result(Status status, @Nullable ForecastResponse data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static Result loading() {
        return new Result(LOADING, null, null);
    }

    public static Result success(@NonNull ForecastResponse data) {
        return new Result(SUCCESS, data, null);
    }

    public static Result error(@NonNull Throwable error) {
        return new Result(ERROR, null, error);
    }
}

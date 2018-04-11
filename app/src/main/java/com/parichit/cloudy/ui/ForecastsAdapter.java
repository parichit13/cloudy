package com.parichit.cloudy.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.parichit.cloudy.R;
import com.parichit.cloudy.data.models.Forecast;
import com.parichit.cloudy.utils.WeatherUtil;

import java.util.ArrayList;
import java.util.List;

public class ForecastsAdapter extends RecyclerView.Adapter<ForecastsAdapter.ViewHolder> {
    private static final String TAG = "ForecastAdapter";
    private List<Forecast.ForecastDay> forecasts;
    private Context context;

    public ForecastsAdapter(Context context) {
        this.context = context;
        this.forecasts = new ArrayList<>();
    }

    @Override
    public ForecastsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForecastsAdapter.ViewHolder holder, int position) {

        Glide.with(context)
                .load("http:"+forecasts.get(position).getDay().getCondition().getIcon())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.icon);

        holder.condition.setText(WeatherUtil.getWeatherName(forecasts.get(position).getDay().getCondition().getCode(),
                forecasts.get(position).getDay().getCondition().getText()));

        holder.temperature.setText(String.valueOf(forecasts.get(position).getDay().getAvgtemp()) + "\u00b0");

        holder.date.setText(WeatherUtil.getRelativeDate(forecasts.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Size: " + forecasts.size());
        return forecasts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date;
        public ImageView icon;
        public TextView condition;
        public TextView temperature;

        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.forecast_date);
            icon = itemView.findViewById(R.id.weather_icon);
            condition = itemView.findViewById(R.id.weather_condition);
            temperature = itemView.findViewById(R.id.weather_temp);
        }
    }

    public void setForecasts(List<Forecast.ForecastDay> forecasts) {
        this.forecasts = forecasts;
    }


}

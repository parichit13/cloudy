package com.parichit.cloudy.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Current implements Serializable{

    @SerializedName("temp_c")
    private double temperature;

    @SerializedName("feelslike_c")
    private double feelslike;

    private Condition condition;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelslike() {
        return feelslike;
    }

    public void setFeelslike(double feelslike) {
        this.feelslike = feelslike;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}

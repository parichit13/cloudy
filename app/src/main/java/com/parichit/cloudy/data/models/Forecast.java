package com.parichit.cloudy.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Forecast {
    private List<ForecastDay> forecastday;

    public List<ForecastDay> getForecastday() {
        return forecastday;
    }

    public void setForecastday(List<ForecastDay> forecastday) {
        this.forecastday = forecastday;
    }

    public class ForecastDay {
        private Date date;
        private Day day;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Day getDay() {
            return day;
        }

        public void setDay(Day day) {
            this.day = day;
        }

        public class Day {
            @SerializedName("maxtemp_c")
            private double maxtemp;

            @SerializedName("avgtemp_c")
            private double avgtemp;

            private Condition condition;

            public double getMaxtemp() {
                return maxtemp;
            }

            public void setMaxtemp(double maxtemp) {
                this.maxtemp = maxtemp;
            }

            public double getAvgtemp() {
                return avgtemp;
            }

            public void setAvgtemp(double avgtemp) {
                this.avgtemp = avgtemp;
            }

            public Condition getCondition() {
                return condition;
            }

            public void setCondition(Condition condition) {
                this.condition = condition;
            }
        }
    }
}

package com.parichit.cloudy.utils;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.US);

    private static final List<Integer> rainCodes = Arrays.asList(
            1063, 1180, 1183, 1186, 1189, 1192,
            1198, 1201, 1240, 1243, 1246
    );

    private static final List<Integer> snowCodes = Arrays.asList(
            1066, 1114, 1117, 1210, 1213, 1216,
            1219, 1222, 1225
    );

    private static final List<Integer> drizzleCodes = Arrays.asList(
            1072, 1150, 1153, 1168, 1171
    );

    private static final List<Integer> sleetCodes = Arrays.asList(
            1069, 1204, 1207, 1249, 1252
    );

    private static final List<Integer> thunderCodes = Arrays.asList(
            1087, 1273, 1276, 1279, 1282
    );

    private static final List<Integer> iceCodes = Arrays.asList(
            1237, 1261, 1264
    );

    public static String getWeatherName(int code, String name) {
        String condition = "";

        if(code == 1003)
            condition = "Clear";
        else if(rainCodes.contains(code))
            condition = "Rain";
        else if(snowCodes.contains(code))
            condition = "Snow";
        else if(drizzleCodes.contains(code))
            condition = "Drizzle";
        else if(sleetCodes.contains(code))
            condition = "Sleet";
        else if(thunderCodes.contains(code))
            condition = "Thunder";
        else if(iceCodes.contains(code))
            condition = "Ice";
        else
            condition = name;

        return condition;
    }

    public static String getRelativeDate(Date date) {
        String result = "";
        if(DateUtils.isToday(date.getTime()))
            result = "Today";
        else if(isTomorrow(date))
            result = "Tomorrow";
        else
            result = sdf.format(date);

        return result;
    }

    public static boolean isTomorrow(Date d) {
        return DateUtils.isToday(d.getTime() - DateUtils.DAY_IN_MILLIS);
    }

    public static String toDegrees(double temp) {
        return ((int)temp) + "\u00b0";
    }

}

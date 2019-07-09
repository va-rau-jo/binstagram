package com.example.binstagram.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatHelper {

    public static String getRelativeTimeAgo(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        String dateString = df.format(date);

        df.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = df.parse(dateString).getTime();
            String longDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

            int spaceIndex = longDate.indexOf(' ');

            relativeDate = longDate.substring(0, spaceIndex + 2).replace(" ", "");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }

    /**
     * Formats large texts nicely (2,000 -> 2k, 5324 -> 5k, etc)
     * @param original The unformatted text
     * @return The formatted string
     */
    public static String formatCounter(int original) {
        int decimals = 1;

        if(original / 1000000 > 0) { // over 1 million
            double divided = (double)original / 1000000;
            return round(divided , decimals) + "m";
        } else if(original / 1000 > 0) { // 1000-999999
            double divided = (double)original / 1000;
            return round(divided, decimals) + "k";
        } else {
            return original + "";
        }
    }

    /**
     * Helper function that rounds the value to however many decimal places
     * @param value Value to be rounded
     * @param places Decimal places
     * @return The formatted double
     */
    private static double round(double value, int places) {
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
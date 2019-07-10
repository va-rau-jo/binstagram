package com.example.binstagram.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
package com.example.binstagram.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatHelper {

    public static String getRelativeTimeAgo(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        String dateString = df.format(date);

        String year = Calendar.getInstance().get(Calendar.YEAR) + "";

        return dateString.endsWith(year)
                ? dateString.substring(0, dateString.length() - 6)
                : dateString;
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
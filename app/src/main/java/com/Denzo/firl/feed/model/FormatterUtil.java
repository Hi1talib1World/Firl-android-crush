package com.Denzo.firl.feed.model;


import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.text.format.Formatter;

import com.Denzo.firl.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FormatterUtil {

    public static String firebaseDBDate = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static String firebaseDBDay = "yyyy-MM-dd";
    public static final long NOW_TIME_RANGE = DateUtils.MINUTE_IN_MILLIS * 5; // 5 minutes

    public static String dateTime = "yyyy-MM-dd HH:mm:ss";

    public static SimpleDateFormat getFirebaseDateFormat() {
        SimpleDateFormat cbDateFormat = new SimpleDateFormat(firebaseDBDate);
        cbDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return cbDateFormat;
    }

    public static String formatFirebaseDay(Date date) {
        SimpleDateFormat cbDateFormat = new SimpleDateFormat(firebaseDBDay);
        cbDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return cbDateFormat.format(date);
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat cbDateFormat = new SimpleDateFormat(dateTime);
        cbDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return cbDateFormat.format(date);
    }

    public static CharSequence getRelativeTimeSpanString(Context context, long time) {
        long now = System.currentTimeMillis();
        long range = Math.abs(now - time);

        if (range < NOW_TIME_RANGE) {
            return context.getString(R.string.now_time_range);
        }

        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
    }

    public static CharSequence getRelativeTimeSpanStringShort(Context context, long time) {
        long now = System.currentTimeMillis();
        long range = Math.abs(now - time);
        return formatDuration(context, range, time);
    }

    private static CharSequence formatDuration(Context context, long range, long time) {
        final Resources res = context.getResources();
        if (range >= DateUtils.WEEK_IN_MILLIS + DateUtils.DAY_IN_MILLIS) {
            return shortFormatEventDay(context, time);
        } else if (range >= DateUtils.WEEK_IN_MILLIS) {
            final int days = (int) ((range + (DateUtils.WEEK_IN_MILLIS / 2)) / DateUtils.WEEK_IN_MILLIS);
            return String.format(res.getString(R.string.duration_week_shortest), days);
        } else if (range >= DateUtils.DAY_IN_MILLIS) {
            final int days = (int) ((range + (DateUtils.DAY_IN_MILLIS / 2)) / DateUtils.DAY_IN_MILLIS);
            return String.format(res.getString(R.string.duration_days_shortest), days);
        } else if (range >= DateUtils.HOUR_IN_MILLIS) {
            final int hours = (int) ((range + (DateUtils.HOUR_IN_MILLIS / 2)) / DateUtils.HOUR_IN_MILLIS);
            return String.format(res.getString(R.string.duration_hours_shortest), hours);
        } else if (range >= NOW_TIME_RANGE) {
            final int minutes = (int) ((range + (DateUtils.MINUTE_IN_MILLIS / 2)) / DateUtils.MINUTE_IN_MILLIS);
            return String.format(res.getString(R.string.duration_minutes_shortest), minutes);
        } else {
            return res.getString(R.string.now_time_range);
        }
    }

    private static String shortFormatEventDay(Context context, long time) {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH;
        Formatter f = new Formatter(new StringBuilder(50), Locale.getDefault());
        return DateUtils.formatDateRange(context, f, time, time, flags).toString();
    }
}

package com.guru.covid19tracker.utils

import android.content.Context
import com.guru.covid19tracker.R

object Utility {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
    private const val WEEK_IN_MILLIS = 7 * DAY_MILLIS.toLong()
    private const val MONTH_IN_MILLIS = 30L * DAY_MILLIS
    private const val YEAR_IN_MILLIS = 365L * DAY_MILLIS

    fun getTimeAgo(time: Long, context: Context): String? {
        var time = time
        if (time < 1000000000000L) { // if timestamp given in seconds, convert to millis
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }
        // TODO: localize
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            context.getString(R.string.time_just_now)
        } else if (diff < 2 * MINUTE_MILLIS) {
            context.getString(R.string.time_min_ago)
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " " + context.getString(
                R.string.time_mins_ago
            )
        } else if (diff < 90 * MINUTE_MILLIS) {
            context.getString(R.string.time_hour_ago)
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " " + context.getString(
                R.string.time_hours_ago
            )
        } else if (diff < 48 * HOUR_MILLIS) {
            context.getString(R.string.time_yesterday)
        } else if (diff < WEEK_IN_MILLIS) {
            (diff / DAY_MILLIS).toString() + " " + context.getString(
                R.string.time_days_ago
            )
        } else if (diff < MONTH_IN_MILLIS) {
            var message = context.getString(R.string.time_week_ago)
            if (diff / WEEK_IN_MILLIS >= 2) {
                message = context.getString(R.string.time_weeks_ago)
            }
            (diff / WEEK_IN_MILLIS).toString() + " " + message
        } else if (diff < YEAR_IN_MILLIS) {
            var message = context.getString(R.string.time_month_ago)
            if (diff / MONTH_IN_MILLIS >= 2) {
                message = context.getString(R.string.time_months_ago)
            }
            (diff / MONTH_IN_MILLIS).toString() + " " + message
        } else {
            var message = context.getString(R.string.time_year_ago)
            if (diff / YEAR_IN_MILLIS >= 2) {
                message = context.getString(R.string.time_years_ago)
            }
            (diff / YEAR_IN_MILLIS).toString() + " " + message
        }
    }
}
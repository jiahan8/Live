package com.example.live.util

import com.example.live.R
import java.util.concurrent.TimeUnit

object DateUtils {

    fun getTimePassedInHourMinSec(resourceProvider: ResourceProvider, timePassedMs: Long): String {
        return when {
            timePassedMs < TimeUnit.MINUTES.toMillis(1) -> {
                resourceProvider.getString(
                    R.string.d_seconds_ago,
                    TimeUnit.MILLISECONDS.toSeconds(timePassedMs)
                )
            }

            timePassedMs < TimeUnit.HOURS.toMillis(1) -> {
                resourceProvider.getString(
                    R.string.d_minutes_ago,
                    TimeUnit.MILLISECONDS.toMinutes(timePassedMs)
                )
            }

            else -> {
                resourceProvider.getString(
                    R.string.d_hours_ago,
                    TimeUnit.MILLISECONDS.toHours(timePassedMs)
                )
            }
        }
    }
}
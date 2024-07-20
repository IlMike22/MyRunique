package com.example.analytics.persentation

import com.example.analytics.domain.AnalyticsValues
import com.example.core.presentation.ui.formatted
import com.example.core.presentation.ui.toFormattedKm
import com.example.core.presentation.ui.toFormattedKmh
import com.example.core.presentation.ui.toFormattedMeters
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

fun Duration.toFormattedTotalTime(): String {
    val days = toLong(DurationUnit.DAYS)
    val hours = toLong(DurationUnit.HOURS) % 24
    val minutes = toLong(DurationUnit.MINUTES) % 60

    return "${days}d ${hours}d ${minutes}m"
}

fun AnalyticsValues.toAnalyticsDashboardState() =
    AnalyticsDashboardState(
        totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
        totalTimeRun = totalTimeRun.toFormattedTotalTime(),
        fastestEverRun = fastestEverRun.toFormattedKmh(),
        averageDistance = (averageDistancePerRun / 1000.0).toFormattedKm(),
        averagePace = averagePacePerRun.seconds.formatted()
    )
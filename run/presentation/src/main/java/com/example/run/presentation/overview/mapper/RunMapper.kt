package com.example.run.presentation.overview.mapper

import com.example.core.domain.run.Run
import com.example.core.presentation.ui.formatted
import com.example.core.presentation.ui.toFormattedKm
import com.example.core.presentation.ui.toFormattedKmh
import com.example.core.presentation.ui.toFormattedMeters
import com.example.core.presentation.ui.toformattedPace
import com.example.run.presentation.overview.models.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {
    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)

    val distanceKm = distanceMeters / 1000.0

    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        averageSpeed = averageSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toformattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl
    )
}
package com.example.core.domain.location

import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt

data class Location(
    val latitude: Double,
    val longitude: Double
) {

    fun distanceTo(other: Location): Float {
        val latDistance = Math.toRadians(other.latitude - latitude)
        val longDistance = Math.toRadians(other.longitude - longitude)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(latitude)) * cos(Math.toRadians(other.latitude)) *
                sin(longDistance / 2) * sin(longDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_METERS * c.toFloat()
    }


    companion object {
        private const val EARTH_RADIUS_METERS = 6_371_000
    }
}






package com.example.core.database.dao

import androidx.room.Query

interface AnalyticsDao {
    @Query("SELECT SUM(distanceMeters) FROM runEntity")
    suspend fun getTotalDistance(): Int

    @Query("SELECT SUM(durationMillis) FROM runEntity")
    suspend fun getTotalTimeRun(): Long

    @Query("SELECT SUM(maxSpeedKmh) FROM runEntity")
    suspend fun getMaxRunSpeed(): Double

    @Query("SELECT AVG(distanceMeters) FROM runEntity")
    suspend fun getAverageDistancePerRun(): Double

    @Query("SELECT AVG(durationMillis / 6000.0 / (distanceMeters / 1000.0)) FROM runEntity")
    suspend fun getAveragePacePerRun(): Double
}
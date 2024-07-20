package com.example.analytics.data

import com.example.analytics.domain.AnalyticsRepository
import com.example.analytics.domain.AnalyticsValues
import com.example.core.database.dao.AnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class RoomAnalyticsRepository(
    private val analyticsDao: AnalyticsDao
) : AnalyticsRepository {
    override suspend fun getAnalyticsValues(): AnalyticsValues {
        return withContext(Dispatchers.IO) {
            val totalDistance = async { analyticsDao.getTotalDistance() }
            val totalTimeMillis = async { analyticsDao.getTotalTimeRun() }
            val maxRunSpeed = async { analyticsDao.getMaxRunSpeed() }
            val averageDistancePerRun = async { analyticsDao.getAverageDistancePerRun() }
            val averagePacePerRun = async { analyticsDao.getAveragePacePerRun() }

            AnalyticsValues(
                totalDistanceRun = totalDistance.await(),
                totalTimeRun = totalTimeMillis.await().milliseconds,
                fastestEverRun = maxRunSpeed.await(),
                averageDistancePerRun = averageDistancePerRun.await(),
                averagePacePerRun = averagePacePerRun.await()
            )
        }
    }
}
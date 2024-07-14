package com.example.run.data

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.example.core.database.dao.RunPendingSyncDao
import com.example.run.domain.SyncRunScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration

class SyncRunWorkerScheduler(
    private val context: Context,
    private val pendingSyncDao: RunPendingSyncDao
) : SyncRunScheduler {
    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(type: SyncRunScheduler.SyncType) {
        TODO("Not yet implemented")
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }

//    private fun scheduleFetchRunsWorker(interval: Duration) {
//        val isSyncScheduled = withContext(Dispatchers.IO) {
//            workManager
//                .getWorkInfosByTag("sync_work")
//                .get()
//                .isNotEmpty()
//        }
//        if (isSyncScheduled) {
//            return
//        }
//
//        val workRequest = PeriodicWorkRequestBuilder<FetchRunsWorker>(
//            repeatInterval = duration
//        )
//    }
}
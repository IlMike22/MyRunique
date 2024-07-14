package com.example.run.domain

import com.example.core.domain.run.Run
import com.example.core.domain.run.RunId
import kotlin.time.Duration

interface SyncRunScheduler {
    suspend fun scheduleSync(type: SyncType)
    suspend fun cancelAllSyncs()


    sealed interface SyncType {
        data class FetchRuns(val interval: Duration): SyncType
        data class DeleteRun(val runId: RunId): SyncType
        class CreateRun(val run: Run, val mapPictureBytes: ByteArray): SyncType
    }
}
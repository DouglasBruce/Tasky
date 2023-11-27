package com.douglasbruce.tasky.core.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.network.Dispatcher
import com.douglasbruce.tasky.core.common.network.TaskyDispatchers.IO
import com.douglasbruce.tasky.core.domain.repository.AgendaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

@HiltWorker
class AgendaSyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val agendaRepository: AgendaRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        if (runAttemptCount >= 3) {
            return@withContext Result.failure()
        }

        return@withContext when (agendaRepository.syncLocalDatabase(
            time = ZonedDateTime.now(),
            updateSelectedDateOnly = false
        )) {
            is AuthResult.Success -> Result.success()
            is AuthResult.Error -> Result.retry()
            is AuthResult.Unauthorized -> Result.failure()
        }
    }

    companion object {
        fun startUpSyncWorkPeriodic() = PeriodicWorkRequestBuilder<DelegatingWorker>(
            repeatInterval = 30L,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
        )
            .setConstraints(SyncConstraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS,
            )
            .setInputData(AgendaSyncWorker::class.delegatedData())
            .build()
    }
}
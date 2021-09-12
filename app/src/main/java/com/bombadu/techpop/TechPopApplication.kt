package com.bombadu.techpop

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.bombadu.techpop.util.DeleteWorker
import com.bombadu.techpop.util.DeleteWorker.Companion.WORK_NAME
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class TechPopApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatRequest = PeriodicWorkRequestBuilder<DeleteWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatRequest
        )
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


}
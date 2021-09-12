package com.bombadu.techpop.util

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bombadu.techpop.local.LocalDao
import com.bombadu.techpop.local.SavedDao
import com.bombadu.techpop.network.NewsApi
import com.bombadu.techpop.repository.DefaultMainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class DeleteWorker @AssistedInject constructor (
    @Assisted applicationContext: Context,
    @Assisted params: WorkerParameters

    ) : CoroutineWorker(applicationContext, params) {


    @Inject
        lateinit var localDao: LocalDao

        @Inject
        lateinit var savedDao: SavedDao


        override suspend fun doWork(): Result {

           // val repository = DefaultMainRepository(newsApi, localDao, savedDao)

            return try {
            //    repository.deleteOldFirebaseData()
                Result.success()

            } catch (e: Exception) {
                Result.retry()
            }
        }

        companion object {
            const val WORK_NAME = "DeleteOldDataFromFirebase"
        }

    }
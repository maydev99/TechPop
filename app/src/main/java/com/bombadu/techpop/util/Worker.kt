package com.bombadu.techpop.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bombadu.techpop.local.LocalDao
import com.bombadu.techpop.local.SavedDao
import com.bombadu.techpop.network.NewsApi
import com.bombadu.techpop.repository.DefaultMainRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Inject


class Worker(
    applicationContext: Context,
    params: WorkerParameters
) :
    CoroutineWorker(applicationContext, params) {


    @Inject
    lateinit var newsApi: NewsApi

    @Inject
    lateinit var localDao: LocalDao

    @Inject
    lateinit var savedDao: SavedDao



    override suspend fun doWork(): Result {
        //val repository = DefaultMainRepository(newsApi, localDao, savedDao, lo)

        return try {
           // repository.getNewsFromFirebase()
            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataFromFirebase"
    }

}
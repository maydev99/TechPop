package com.bombadu.techpop.repository

import androidx.lifecycle.LiveData
import com.bombadu.techpop.local.LocalDao
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.local.SavedDao
import com.bombadu.techpop.local.SavedEntity
import com.bombadu.techpop.network.NewsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultMainRepository @Inject constructor(
    val newsApi: NewsApi,
    val localDao: LocalDao,
    val savedDao: SavedDao
) : MainRepository {


    override fun observeAllNewsData(): LiveData<List<NewsEntity>> {
        return localDao.getAllNewsArticlesSortedByPublishDate()
    }

    override suspend fun insertNewsArticles(newsEntity: NewsEntity) {
        withContext(Dispatchers.IO) {
            localDao.insertNewData(newsEntity)
        }

    }

    override suspend fun deleteOldArticlesFromLocalDB(deleteTime: Long) {
        withContext(Dispatchers.IO) {
            localDao.deleteOldArticlesByDeleteTimestamp(deleteTime)
        }

    }

    override suspend fun insertSavedArticle(savedEntity: SavedEntity) {
        withContext(Dispatchers.IO) {
            savedDao.insertSavedData(savedEntity)
        }
    }

    override suspend fun deleteSavedArticle(savedEntity: SavedEntity) {
        withContext(Dispatchers.IO) {
            savedDao.deleteSavedArticle(savedEntity)
        }
    }


    override fun observeAllSavedArticles(): LiveData<List<SavedEntity>> {
        return savedDao.getAllSavedArticles()
    }


    companion object {
        val TAG: String = DefaultMainRepository::class.java.simpleName
    }
}




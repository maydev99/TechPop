package com.bombadu.techpop.repository


import androidx.lifecycle.LiveData
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.local.SavedEntity


interface MainRepository {

    suspend fun deleteOldLocalData()

    suspend fun getNewsFromFirebase()

    fun observeAllNewsData(): LiveData<List<NewsEntity>>

    suspend fun insertSavedArticle(savedEntity: SavedEntity)

    suspend fun deleteSavedArticle(savedEntity: SavedEntity)

    fun observeAllSavedArticles() : LiveData<List<SavedEntity>>


}
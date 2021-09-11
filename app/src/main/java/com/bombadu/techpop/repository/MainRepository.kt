package com.bombadu.techpop.repository


import androidx.lifecycle.LiveData
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.local.SavedEntity


interface MainRepository {

    suspend fun checkUpdate()


    suspend fun getCurrentFirebaseData(sources: String)

    suspend fun getNewDataFromApi(sources: String, oldTitleList: MutableList<String>)


    suspend fun addNewDataToFirebase(updateList: List<NewsEntity>)

    suspend fun deleteOldFirebaseData()

    suspend fun deleteOldLocalData()

    suspend fun getNewsFromFirebase()

    fun observeAllNewsData(): LiveData<List<NewsEntity>>

    suspend fun setFirebaseTimeTimestamp()


    suspend fun insertSavedArticle(savedEntity: SavedEntity)

    suspend fun deleteSavedArticle(savedEntity: SavedEntity)

    fun observeAllSavedArticles() : LiveData<List<SavedEntity>>


}
package com.bombadu.techpop.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.bombadu.techpop.local.LocalDao
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.local.SavedDao
import com.bombadu.techpop.local.SavedEntity
import com.bombadu.techpop.network.NewsApi
import com.bombadu.techpop.util.Constants.ARTICLE_LIFE_SPAN_IN_DAYS
import com.bombadu.techpop.util.NetworkUtil
import com.bombadu.techpop.util.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultMainRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val localDao: LocalDao,
    private val savedDao: SavedDao
) : MainRepository {

    private val rootRef = Firebase.database.reference
    private val v2Ref = rootRef.child("v2")
    private val clientUpdatable = v2Ref.child("client_updatable")
    private val articlesRef = clientUpdatable.child("articles")
    private val timingRef = clientUpdatable.child("timing")

    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }

    override suspend fun checkUpdate() {

        val intervalListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updateInterval = snapshot.child("interval").value.toString().toLong()
                val updateTimestamp = snapshot.child("last_api_update").value.toString().toLong()
                val sources = snapshot.child("sources").value.toString()
                val nextUpdateTime = updateTimestamp + updateInterval
                val currentTime = Utils.getTimeStamp().toLong()

                if (currentTime > nextUpdateTime) {
                    ioScope.launch {
                        getCurrentFirebaseData(sources)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database Error: $error")
            }

        }

        timingRef.addListenerForSingleValueEvent(intervalListener)

    }


    override suspend fun getCurrentFirebaseData(sources: String) {
        val oldTitleList = mutableListOf<String>()

        val articlesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val key = item.key.toString()
                    val title = snapshot.child(key).child("title").value.toString()
                    oldTitleList.add(title)

                }
                ioScope.launch {
                    getNewDataFromApi(sources, oldTitleList)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "GetCurrentFirebaseData: $error")
            }

        }

        articlesRef.addListenerForSingleValueEvent(articlesListener)


    }

    override suspend fun getNewDataFromApi(sources: String, oldTitleList: MutableList<String>) {
        val updateList = mutableListOf<NewsEntity>()
        val networkData = newsApi.getTopHeadlines(sources)
        val convertedData = NetworkUtil.convertNewsData(networkData)
        for (i in convertedData.indices) {
            val author = convertedData[i].author
            val content = convertedData[i].content
            val description = convertedData[i].description
            val publishedAt = convertedData[i].publishedAt
            val source = convertedData[i].source
            val title = convertedData[i].title
            val url = convertedData[i].url
            val urlToImage = convertedData[i].urlToImage
            val timestamp = convertedData[i].timeStamp

            val newsItem = NewsEntity(
                author, content, description, publishedAt, source, title, url, urlToImage, timestamp
            )

            if (!oldTitleList.contains(title)) {
                updateList.add(newsItem)
                oldTitleList.add(title!!)

            }

        }

        ioScope.launch {
            addNewDataToFirebase(updateList)
        }

    }

    override suspend fun addNewDataToFirebase(updateList: List<NewsEntity>) {
        for (i in updateList.indices) {
            val taskMap: MutableMap<String, Any> = HashMap()
            taskMap.clear()
            taskMap["author"] = updateList[i].author.toString()
            taskMap["content"] = updateList[i].content.toString()
            taskMap["description"] = updateList[i].description.toString()
            taskMap["published_at"] = updateList[i].publishedAt.toString()
            taskMap["source"] = updateList[i].source.toString()
            taskMap["title"] = updateList[i].title.toString()
            taskMap["url"] = updateList[i].url.toString()
            taskMap["url_to_image"] = updateList[i].urlToImage.toString()
            taskMap["time_stamp"] = updateList[i].timeStamp.toString()

            articlesRef.push().updateChildren(taskMap)
        }


        setFirebaseTimeTimestamp()
        getNewsFromFirebase()


    }

    override suspend fun deleteOldFirebaseData() {
        Log.i(TAG, "Deleting Old Firebase Data")
        val deleteArticlesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val currentTs = Utils.getTimeStamp().toLong()
                val deleteTs = currentTs - Utils.convertDaysToTimestampTime(1)
                val deleteList = mutableListOf<String>()

                for (item in snapshot.children) {
                    val key = item.key.toString()
                    val fbTimestamp = snapshot.child(key).child("time_stamp").value.toString().toLong()
                    if (fbTimestamp < deleteTs) {
                        deleteList.add(key)
                    }
                }

                for (i in deleteList.indices) {
                    articlesRef.child(deleteList[i]).removeValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Firebase Delete Failed")
            }
        }

        articlesRef.addListenerForSingleValueEvent(deleteArticlesListener)
    }

    override suspend fun deleteOldLocalData() {
        Log.i(TAG, "Deleting Old Local Data")
        val deleteTime = Utils.getTimeStamp().toLong() - Utils.convertDaysToTimestampTime(ARTICLE_LIFE_SPAN_IN_DAYS)
        ioScope.launch {
            localDao.deleteOldArticlesByDeleteTimestamp(deleteTime)
        }
    }

    override suspend fun getNewsFromFirebase() {

        val updateLocalList = mutableListOf<NewsEntity>()
        //val fbDeleteList = mutableListOf<String>()
        //val localDeleteList = mutableListOf<String>()
        Log.i(
            TAG,
            "GETTING NEWS FROM FIREBASE ${Utils.convertTimestampToDate(Utils.getTimeStamp())}"
        )
        var myCount = 0

        val articlesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                for (item in snapshot.children) {
                    val key = item.key.toString()
                    val author = snapshot.child(key).child("author").value.toString()
                    val content = snapshot.child(key).child("content").value.toString()
                    val description = snapshot.child(key).child("description").value.toString()
                    val publishedAt = snapshot.child(key).child("published_at").value.toString()
                    val source = snapshot.child(key).child("source").value.toString()
                    val title = snapshot.child(key).child("title").value.toString()
                    val url = snapshot.child(key).child("url").value.toString()
                    val urlToImage = snapshot.child(key).child("url_to_image").value.toString()
                    val timeStamp = snapshot.child(key).child("time_stamp").value.toString()


                    val newInsert = NewsEntity(
                        author, content, description, publishedAt,
                        source, title, url, urlToImage, timeStamp
                    )

                    updateLocalList.add(newInsert)

                    myCount++
                }

                Log.i(TAG, "FB COUNT: $myCount")
                myCount = 0

                val duplicatesRemoved: List<NewsEntity> = updateLocalList.toSet().toList()
                for (i in duplicatesRemoved.indices) {
                    val author = duplicatesRemoved[i].author
                    val content = duplicatesRemoved[i].content
                    val description = duplicatesRemoved[i].description
                    val publishedAt = duplicatesRemoved[i].publishedAt
                    val source = duplicatesRemoved[i].source
                    val title = duplicatesRemoved[i].title
                    val url = duplicatesRemoved[i].url
                    val urlToImage = duplicatesRemoved[i].urlToImage
                    val timestamp = duplicatesRemoved[i].timeStamp

                    val localDBInsert = NewsEntity(
                        author,
                        content,
                        description,
                        publishedAt,
                        source,
                        title,
                        url,
                        urlToImage,
                        timestamp
                    )

                    ioScope.launch {
                        if (!localDao.doesTitleExist(title!!)) {
                            localDao.insertNewData(localDBInsert)
                        }

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database Error: $error")
            }
        }

        articlesRef.addValueEventListener(articlesListener)


    }


    override fun observeAllNewsData(): LiveData<List<NewsEntity>> {
        return localDao.getAllNewsArticlesSortedByPublishDate()
    }


    override suspend fun setFirebaseTimeTimestamp() {
        val timestamp = Utils.getTimeStamp()
        timingRef.child("last_api_update").setValue(timestamp)

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




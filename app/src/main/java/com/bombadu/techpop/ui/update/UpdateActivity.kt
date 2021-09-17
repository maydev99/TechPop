package com.bombadu.techpop.ui.update

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bombadu.techpop.databinding.ActivityUpdateBinding
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.network.NewsApi
import com.bombadu.techpop.repository.DefaultMainRepository
import com.bombadu.techpop.repository.MainRepository
import com.bombadu.techpop.ui.MainActivity
import com.bombadu.techpop.util.Constants.ARTICLE_LIFE_SPAN_IN_DAYS
import com.bombadu.techpop.util.NetworkUtil
import com.bombadu.techpop.util.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    @Inject
    lateinit var repository: MainRepository
    @Inject
    lateinit var newsApi: NewsApi
    var articleLifeSpanDays = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkUpdate()


    }

    private val rootRef = Firebase.database.reference
    private val v2Ref = rootRef.child("v2")
    private val clientUpdatable = v2Ref.child("client_updatable")
    private val articlesRef = clientUpdatable.child("articles")
    private val timingRef = clientUpdatable.child("timing")
    val ioScope by lazy { CoroutineScope(Dispatchers.IO) }


    private fun checkUpdate() {

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
                } else {
                    startActivity(Intent(this@UpdateActivity, MainActivity::class.java))
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(DefaultMainRepository.TAG, "Database Error: $error")
            }

        }

        timingRef.addListenerForSingleValueEvent(intervalListener)

    }


    private fun getCurrentFirebaseData(sources: String) {
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
                Log.e(DefaultMainRepository.TAG, "GetCurrentFirebaseData: $error")
            }
        }

        articlesRef.addListenerForSingleValueEvent(articlesListener)


    }

    suspend fun getNewDataFromApi(sources: String, oldTitleList: MutableList<String>) {
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

    private fun addNewDataToFirebase(updateList: List<NewsEntity>) {
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
        deleteOldFirebaseData()


    }

    private fun deleteOldFirebaseData() {
        Log.i(DefaultMainRepository.TAG, "Deleting Old Firebase Data")
        val deleteArticlesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val currentTs = Utils.getTimeStamp().toLong()
                val deleteTs = currentTs - Utils.convertDaysToTimestampTime(ARTICLE_LIFE_SPAN_IN_DAYS)
                val deleteList = mutableListOf<String>()

                for (item in snapshot.children) {
                    val key = item.key.toString()
                    val fbTimestamp =
                        snapshot.child(key).child("time_stamp").value.toString().toLong()
                    if (fbTimestamp < deleteTs) {
                        deleteList.add(key)
                    }
                }

                for (i in deleteList.indices) {
                    articlesRef.child(deleteList[i]).removeValue()
                }

                startActivity(Intent(this@UpdateActivity, MainActivity::class.java))
                finish()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(DefaultMainRepository.TAG, "Firebase Delete Failed")
            }


        }

        articlesRef.addListenerForSingleValueEvent(deleteArticlesListener)


    }

    private fun setFirebaseTimeTimestamp() {
        val timestamp = Utils.getTimeStamp()
        timingRef.child("last_api_update").setValue(timestamp)

    }
}
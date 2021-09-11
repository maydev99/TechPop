package com.bombadu.techpop.util

import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.network.NewsData

object NetworkUtil {



    fun convertNewsData(newsData: NewsData) : List<NewsEntity> {
        val item = newsData.articles
        val myList = mutableListOf<NewsEntity>()
        for (i in item.indices) {
            val author = item[i].author ?: "Anonymous"
            val content = item[i].content ?: ""
            val description = item[i].description ?: ""
            val publishAt = item[i].publishedAt ?: ""
            val source = item[i].source?.name ?: ""
            val title = item[i].title ?: ""
            val url = item[i].url ?: ""
            val urlToImage = item[i].urlToImage ?: ""
            val timeStamp = Utils.getTimeStamp()

            myList.add(
                NewsEntity(
                author,
                content,
                description,
                publishAt,
                source,
                title,
                url,
                urlToImage,
                timeStamp)
            )
        }

        return myList
    }
}

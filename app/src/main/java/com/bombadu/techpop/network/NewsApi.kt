package com.bombadu.techpop.network


import com.bombadu.techpop.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("sources", encoded = true) sources: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    ): NewsData
}


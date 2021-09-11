package com.bombadu.techpop.di

import android.content.Context
import androidx.room.Room
import com.bombadu.techpop.local.LocalDao
import com.bombadu.techpop.local.LocalDatabase
import com.bombadu.techpop.local.SavedDao
import com.bombadu.techpop.network.NewsApi
import com.bombadu.techpop.repository.DefaultMainRepository
import com.bombadu.techpop.repository.MainRepository
import com.bombadu.techpop.util.Constants.BASE_URL
import com.bombadu.techpop.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
               level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideDefaultMainRepository(
        api: NewsApi,
        dao: LocalDao,
        sDao: SavedDao,
    ) = DefaultMainRepository(api, dao, sDao) as MainRepository


    @Singleton
    @Provides
    fun provideNewApi(): NewsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(providesClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLocalDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideLocalDao(
        database: LocalDatabase
    ) = database.localDao()

    @Singleton
    @Provides
    fun provideSavedDao(
        database: LocalDatabase
    ) = database.savedDao()

}
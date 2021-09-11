package com.bombadu.techpop.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewData(newsEntity: NewsEntity)

    @Query("DELETE FROM news_data_table")
    suspend fun deleteAllNewsArticles()

    @Query("SELECT * FROM news_data_table")
    fun getAllNewsArticles(): LiveData<List<NewsEntity>>

    @Query("SELECT * FROM news_data_table ORDER BY publishedAt DESC")
    fun getAllNewsArticlesSortedByPublishDate(): LiveData<List<NewsEntity>>

    @Query("SELECT EXISTS(SELECT * FROM news_data_table WHERE title = :title)")
    fun doesTitleExist(title: String) : Boolean

    @Query("DELETE FROM news_data_table WHERE title = :title")
    fun deleteArticleByTitle(title: String)

    @Query("DELETE FROM news_data_table WHERE timeStamp < :deleteTimestamp")
    fun deleteOldArticlesByDeleteTimestamp(deleteTimestamp: Long)
}

@Dao
interface SavedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedData(savedEntity: SavedEntity)

    @Delete
    fun deleteSavedArticle(savedEntity: SavedEntity)


    @Query("SELECT * FROM saved_data_table ORDER BY savedTimeStamp DESC")
    fun getAllSavedArticles(): LiveData<List<SavedEntity>>

}

@Database(
    entities = [NewsEntity::class, SavedEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun localDao(): LocalDao
    abstract fun savedDao(): SavedDao

}
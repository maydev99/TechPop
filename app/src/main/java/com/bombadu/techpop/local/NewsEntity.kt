package com.bombadu.techpop.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "news_data_table")
@Parcelize
data class NewsEntity(
    @ColumnInfo(name = "author") var author: String?,
    @ColumnInfo(name = "content") var content: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "publishedAt") var publishedAt: String?,
    @ColumnInfo(name = "source") var source: String?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "url") var url: String?,
    @ColumnInfo(name = "urlToImage") var urlToImage: String?,
    @ColumnInfo(name = "timeStamp") var timeStamp: String?
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

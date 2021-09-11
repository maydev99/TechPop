package com.bombadu.techpop.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "saved_data_table")
@Parcelize
data class SavedEntity(
    @ColumnInfo(name ="savedTimeStamp") var savedTimeStamp: String?,
    @ColumnInfo(name = "webUrl") var webUrl: String?,
    @ColumnInfo(name = "imageUrl") var imageUrl: String?,
    @ColumnInfo(name = "title") var title: String?

) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


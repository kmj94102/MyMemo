package com.example.mymemo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(
        value = [
            "index", "title", "contents", "isSecret",
            "password", "timestamp", "colorGroup", "isImportance"], unique = true
    )]
)
data class MemoEntity(
    @PrimaryKey(autoGenerate = true) val index: Long = 0,
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "contents") val contents: String = "",
    @ColumnInfo(name = "isSecret") val isSecret: Boolean = false,
    @ColumnInfo(name = "isImportance") val isImportance: Boolean = false,
    @ColumnInfo(name = "password") val password: String = "",
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "colorGroup") val colorGroup: Int = 0
) {
    fun mapper() = MemoItem(
        index = index,
        title = title,
        contents = contents,
        isSecret = isSecret,
        isImportance = isImportance,
        password = password,
        colorGroup = colorGroup
    )
}


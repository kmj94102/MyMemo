package com.example.mymemo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MemoEntity::class],
    version = 1
)
abstract class MemoDatabase : RoomDatabase() {

    abstract fun memoDao(): MemoDao

    companion object {
        const val DATABASE_NAME = "my_memo.db"
    }

}
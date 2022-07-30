package com.example.mymemo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoItem(memoEntity: MemoEntity): Long

    @Query("SELECT * FROM MemoEntity")
    fun selectAllMemo(): Flow<List<MemoEntity>>

    @Query("SELECT * FROM MemoEntity WHERE `index` = :index")
    fun selectMemoIndex(index: Long): Flow<MemoEntity>

    @Query("DELETE FROM MemoEntity WHERE `index` = :index")
    suspend fun deleteMemoIndex(index: Long)

    @Query("UPDATE  MemoEntity SET `isImportance` = :isImportance WHERE `index` = :index")
    suspend fun updateImportance(index: Long, isImportance: Boolean): Int

}
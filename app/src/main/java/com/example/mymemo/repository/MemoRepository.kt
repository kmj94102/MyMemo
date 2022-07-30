package com.example.mymemo.repository

import com.example.mymemo.data.MemoDao
import com.example.mymemo.data.MemoEntity
import com.example.mymemo.data.MemoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class MemoRepository @Inject constructor(
    private val db: MemoDao
) {

    suspend fun insertMemoItem(memoEntity: MemoItem): Long =
        db.insertMemoItem(memoEntity.mapper())

    fun selectAllMemo(): Flow<List<MemoItem>> =
        db.selectAllMemo().map { it.map { entity -> entity.mapper() } }

    fun selectMemoIndex(index: Long): Flow<MemoItem> =
        db.selectMemoIndex(index).map { it.mapper() }

    suspend fun deleteMemoIndex(index: Long) =
        db.deleteMemoIndex(index)

}
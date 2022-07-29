package com.example.mymemo.view.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymemo.data.MemoDao
import com.example.mymemo.data.MemoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MemoListViewModel @Inject constructor(
    private val db: MemoDao
) : ViewModel() {

    val list = MutableStateFlow<List<MemoEntity>>(emptyList())

    fun selectAllMemo() {
        db.selectAllMemo()
            .onEach { list.value = it }
            .onEmpty { list.value = emptyList() }
            .catch { list.value = emptyList() }
            .launchIn(viewModelScope)
    }

}
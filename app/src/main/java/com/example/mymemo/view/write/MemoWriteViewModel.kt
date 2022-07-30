package com.example.mymemo.view.write

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymemo.data.MemoItem
import com.example.mymemo.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MemoWriteViewModel @Inject constructor(
    private val repository: MemoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _memoItem = mutableStateOf(MemoItem())
    val memoItemState : State<MemoItem> = _memoItem

    init {
        savedStateHandle.get<Long>("index")?.let {
            selectMemo(it)
        }
    }

    private fun insertMemo(
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = viewModelScope.launch {
        val result = repository.insertMemoItem(_memoItem.value)

        if (result == -1L) failureListener() else successListener()
    }

    private fun selectMemo(
        index: Long
    ) {
        repository.selectMemoIndex(index)
            .onEach {
                _memoItem.value = it
            }
            .launchIn(viewModelScope)
    }

    private fun updateMemo(
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = viewModelScope.launch {
        val result = repository.updateMemo(_memoItem.value)
        if (result == -1L) failureListener() else successListener()
    }

    fun event(event: WriteEvent) {
        when(event) {
            is WriteEvent.SelectMemo -> {
                Log.e("++++++", "호출")
                selectMemo(event.index)
            }
            is WriteEvent.WriteTitle -> {
                _memoItem.value = _memoItem.value.copy(
                    title = event.title
                )
            }
            is WriteEvent.WriteContents -> {
                _memoItem.value = _memoItem.value.copy(
                    contents = event.contents
                )
            }
            is WriteEvent.WritePassword -> {
                _memoItem.value = _memoItem.value.copy(
                    password = event.password
                )
            }
            is WriteEvent.ChangeSecretMode -> {
                _memoItem.value = _memoItem.value.copy(
                    isSecret = event.isSecret
                )
            }
            is WriteEvent.ChangeColorGroup -> {
                _memoItem.value = _memoItem.value.copy(
                    colorGroup = event.colorGroup
                )
            }
            is WriteEvent.InsertMemo -> {
                insertMemo(event.successListener, event.failureListener)
            }
            is WriteEvent.UpdateMemo -> {
                updateMemo(event.successListener, event.failureListener)
            }
        }
    }

}
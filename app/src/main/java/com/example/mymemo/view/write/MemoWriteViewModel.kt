package com.example.mymemo.view.write

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymemo.data.MemoItem
import com.example.mymemo.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoWriteViewModel @Inject constructor(
    private val repository: MemoRepository
) : ViewModel() {

    private val _memoItem = mutableStateOf(MemoItem())
    val memoItemState : State<MemoItem> = _memoItem

    fun insertMemo(
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = viewModelScope.launch {
        val result = repository.insertMemoItem(memoEntity = _memoItem.value)

        if (result == -1L) failureListener() else successListener()
    }

    fun selectMemo(
        index: Long
    ) {
        repository.selectMemoIndex(index)
            .onEach {
                _memoItem.value = it
            }
            .launchIn(viewModelScope)
    }

    fun event(event: WriteEvent) {
        when(event) {
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
        }
    }

}
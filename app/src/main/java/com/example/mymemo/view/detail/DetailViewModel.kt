package com.example.mymemo.view.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymemo.data.MemoItem
import com.example.mymemo.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MemoRepository
) : ViewModel() {

    private val _memoItemState = mutableStateOf(MemoItem())
    val memoITemState: State<MemoItem> = _memoItemState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow

    private fun selectMemo(index: Long) {
        repository.selectMemoIndex(index)
            .onEach { _memoItemState.value = it }
            .onEmpty {
                _eventFlow.emit(
                    UiEvent.Error("메모 조회를 실패하였습니다.")
                )
            }
            .launchIn(viewModelScope)
    }

    private fun updateImportance(
        index: Long,
        isImportance: Boolean,
    ) = viewModelScope.launch {
        val result = repository.updateImportance(index, isImportance)

        if (result == -1) {
            _eventFlow.emit(UiEvent.Error("오류 발생"))
        }
    }

    private fun deleteMemo(
        index: Long,
        listener: () -> Unit
    ) = viewModelScope.launch {
        repository.deleteMemoIndex(index = index)
        listener()
    }

    fun event(event: DetailEvent) {
        when(event) {
            is DetailEvent.SearchMemo -> {
                selectMemo(event.index)
            }
            is DetailEvent.UpdateImportance -> {
                updateImportance(event.index, event.isImportance)
            }
            is DetailEvent.DeleteMemo -> {
                deleteMemo(event.index, event.listener)
            }
        }
    }

    sealed class UiEvent {
        data class Error(val msg: String): UiEvent()
    }

}
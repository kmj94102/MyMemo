package com.example.mymemo.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymemo.data.MemoDao
import com.example.mymemo.data.MemoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val db: MemoDao
) : ViewModel() {

    val statusState = MutableStateFlow<Event>(Event.Init)

    fun selectMemo(index: Long) {
        db.selectMemoIndex(index)
            .onEach { statusState.value = Event.Success(it) }
            .onEmpty { statusState.value = Event.Failure }
            .catch { statusState.value = Event.Failure }
            .launchIn(viewModelScope)
    }

    fun deleteMemo(index: Long) = viewModelScope.launch {
        db.deleteMemoIndex(index = index)
    }

    sealed class Event {
        object Init: Event()
        data class Success(
            val memoItem: MemoEntity
        ): Event()
        object Failure: Event()
    }

}
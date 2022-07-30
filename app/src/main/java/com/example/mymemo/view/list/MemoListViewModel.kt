package com.example.mymemo.view.list

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
class MemoListViewModel @Inject constructor(
    private val repository: MemoRepository
) : ViewModel() {

    private val _list = mutableStateOf<List<MemoItem>>(emptyList())
    val list : State<List<MemoItem>> = _list

    private var _searchState = mutableStateOf("")
    val searchState: State<String> = _searchState

    private var _queryState = mutableStateOf(0)
    val queryState: State<Int> = _queryState

    init {
        selectAllMemo()
    }

    private fun selectAllMemo(search: String = "%%") {
        repository.selectAllMemo(search)
            .map {
                orderByList(it)
            }
            .onEach { _list.value = it }
            .onEmpty { _list.value = emptyList() }
            .catch { _list.value = emptyList() }
            .launchIn(viewModelScope)
    }

    private fun orderByList(list: List<MemoItem>) : List<MemoItem> {
        return when(_queryState.value) {
            // 날짜순
            0 -> {
                list.sortedBy {
                    it.timestamp
                }
            }
            // 타이틀순
            1 -> {
                list.sortedBy {
                    it.title
                }
            }
            // 중요글만
            2 -> {
                list.filter {
                    it.isImportance
                }
            }
            // 비밀글만
            else -> {
                list.filter {
                    it.isSecret
                }
            }
        }
    }

    private fun deleteMemo(
        index: Long
    ) = viewModelScope.launch {
        repository.deleteMemoIndex(index)
    }

    private fun updateImportance(
        index: Long,
        isImportance: Boolean
    ) = viewModelScope.launch {
        repository.updateImportance(index, isImportance)
    }

    fun event(event: ListEvent) {
        when(event) {
            is ListEvent.WriteSearch -> {
                _searchState.value = event.search
                selectAllMemo("%${_searchState.value}%")
            }
            is ListEvent.ChangeQuery -> {
                _queryState.value = event.queryIndex
                selectAllMemo("%${_searchState.value}%")
            }
            is ListEvent.DeleteMemo -> {
                deleteMemo(event.index)
            }
            is ListEvent.UpdateImportance -> {
                updateImportance(event.index, event.isImportance)
            }
        }
    }

}
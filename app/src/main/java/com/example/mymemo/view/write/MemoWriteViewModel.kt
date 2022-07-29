package com.example.mymemo.view.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymemo.data.MemoDao
import com.example.mymemo.data.MemoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoWriteViewModel @Inject constructor(
    private val db: MemoDao
) : ViewModel() {

    val statusState = MutableStateFlow<InsertEvent>(InsertEvent.Init)
    val titleState = MutableStateFlow("")
    val contentsState = MutableStateFlow("")
    val isSecretState = MutableStateFlow(false)
    val passwordState = MutableStateFlow("")
    val colorGroupState = MutableStateFlow(0)

    fun insertMemo() = viewModelScope.launch {

        if (titleState.value.isEmpty()) {
            statusState.emit(InsertEvent.EmptyTitle)
            return@launch
        }

        if (contentsState.value.isEmpty()) {
            statusState.emit(InsertEvent.EmptyContents)
            return@launch
        }

        if (isSecretState.value && passwordState.value.isEmpty()) {
            statusState.emit(InsertEvent.EmptyPassword)
            return@launch
        }

        val result = db.insertMemoItem(
            MemoEntity(
                title = titleState.value,
                contents = contentsState.value,
                isSecret = isSecretState.value,
                password = passwordState.value,
                colorGroup = colorGroupState.value
            )
        )

        if (result == -1L) {
            statusState.emit(InsertEvent.Failure)
        } else {
            statusState.emit(InsertEvent.Success)
        }

    }

    sealed class InsertEvent {
        object Init: InsertEvent()
        object Failure: InsertEvent()
        object Success: InsertEvent()
        object EmptyTitle: InsertEvent()
        object EmptyContents: InsertEvent()
        object EmptyPassword: InsertEvent()
    }

}
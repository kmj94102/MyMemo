package com.example.mymemo.view.detail

sealed class DetailEvent {
    data class UpdateImportance(
        val index: Long,
        val isImportance: Boolean,
    ) : DetailEvent()
    data class DeleteMemo(
        val index: Long,
        val listener: () -> Unit
    ) : DetailEvent()
}

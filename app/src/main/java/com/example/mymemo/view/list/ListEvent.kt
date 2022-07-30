package com.example.mymemo.view.list

sealed class ListEvent {
    data class WriteSearch(
        val search: String
    ): ListEvent()
    data class ChangeQuery(
        val queryIndex: Int
    ): ListEvent()
    data class DeleteMemo(
        val index: Long
    ): ListEvent()
    data class UpdateImportance(
        val index: Long,
        val isImportance: Boolean
    ): ListEvent()
}

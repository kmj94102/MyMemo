package com.example.mymemo.view.write

sealed class WriteEvent {
    data class SelectMemo(val index: Long): WriteEvent()
    data class WriteTitle(val title: String): WriteEvent()
    data class WriteContents(val contents: String): WriteEvent()
    data class WritePassword(val password: String): WriteEvent()
    data class ChangeSecretMode(val isSecret: Boolean): WriteEvent()
    data class ChangeColorGroup(val colorGroup: Int): WriteEvent()
    data class InsertMemo(
        val successListener: () -> Unit,
        val failureListener: () -> Unit
    ): WriteEvent()
    data class UpdateMemo(
        val successListener: () -> Unit,
        val failureListener: () -> Unit
    ): WriteEvent()
}
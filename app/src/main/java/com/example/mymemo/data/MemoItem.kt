package com.example.mymemo.data

data class MemoItem(
    val index: Long = 0,
    var title: String = "",
    var contents: String = "",
    var isSecret: Boolean = false,
    var isImportance: Boolean = false,
    var password: String = "",
    var colorGroup: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun mapper() = MemoEntity(
        index = index,
        title = title,
        contents = contents,
        isSecret = isSecret,
        isImportance = isImportance,
        password = password,
        colorGroup = colorGroup,
        timestamp = timestamp
    )
}

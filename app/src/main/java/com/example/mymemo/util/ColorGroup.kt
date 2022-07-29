package com.example.mymemo.util

enum class ColorGroup(val index: Int, val mainColor: Long, val subColor: Long) {

    Group1(0, 0xFFF37878, 0xFFFFBEBE),
    Group2(1, 0xFFF8BB54, 0xFFFAD9A1),
    Group3(2, 0xFF96E263, 0xFFD9F8C4),
    Group4(3, 0xFFFFE924, 0xFFFCFF79),

}

fun getMainColor(index: Int) =
    ColorGroup.values().find { it.index == index }?.mainColor ?: 0

fun getSubColor(index: Int) =
    ColorGroup.values().find { it.index == index }?.subColor ?: 0

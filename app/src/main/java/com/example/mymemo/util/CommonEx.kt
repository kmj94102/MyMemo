package com.example.mymemo.util

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(res: Int) {
    Toast.makeText(this, getString(res), Toast.LENGTH_SHORT).show()
}

fun dateFromTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd")
    return dateFormat.format(timestamp)
}
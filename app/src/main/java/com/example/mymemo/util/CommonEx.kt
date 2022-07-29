package com.example.mymemo.util

import android.content.Context
import android.widget.Toast

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(res: Int) {
    Toast.makeText(this, getString(res), Toast.LENGTH_SHORT).show()
}
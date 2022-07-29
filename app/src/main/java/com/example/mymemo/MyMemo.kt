package com.example.mymemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyMemo : Application() {

    companion object {
        private lateinit var application : MyMemo
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}
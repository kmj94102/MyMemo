package com.example.mymemo.di

import android.content.Context
import androidx.room.Room
import com.example.mymemo.data.MemoDao
import com.example.mymemo.data.MemoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): MemoDatabase =
        Room.databaseBuilder(context, MemoDatabase::class.java, MemoDatabase.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideMemoDao(
        database: MemoDatabase
    ): MemoDao =
        database.memoDao()

}
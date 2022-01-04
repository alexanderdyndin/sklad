package com.work.sklad.di

import android.content.Context
import androidx.room.Room
import com.work.sklad.App
import com.work.sklad.data.database.SkladDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "SKLAD_DATABASE"

    @Provides
    fun getDatabaseDao(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, SkladDatabase::class.java, DB_NAME)
            .build()
            .skladDAO()
}
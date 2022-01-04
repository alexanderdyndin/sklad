package com.work.sklad

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.work.sklad.data.database.SkladDatabase
import com.work.sklad.data.model.Product
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*

@HiltAndroidApp
class App: Application() {


    override fun onCreate() {
        super.onCreate()

    }

}
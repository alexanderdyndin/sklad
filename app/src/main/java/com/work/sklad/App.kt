package com.work.sklad

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.work.sklad.data.database.SkladDatabase
import com.work.sklad.data.model.Product
import kotlinx.coroutines.*


class App: Application() {

    lateinit var skladDatabase: SkladDatabase

    companion object {
        const val DB_NAME = "SKLAD_DATABASE"
    }

    override fun onCreate() {
        super.onCreate()
        skladDatabase = Room.databaseBuilder(
            applicationContext, SkladDatabase::class.java, DB_NAME
        ).build()

        val userDao = skladDatabase.skladDAO()
        Log.e("123", skladDatabase.toString())
        GlobalScope.launch {
            withContext(Dispatchers.IO){
               userDao.insert(Product(1, "qwer", "123", 2.22))
            }
        }

    }

}
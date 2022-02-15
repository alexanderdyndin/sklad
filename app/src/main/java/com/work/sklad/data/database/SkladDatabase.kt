package com.work.sklad.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.work.sklad.data.dao.SkladDao
import com.work.sklad.data.model.*
import com.work.sklad.data.utils.Converters

@Database(entities = [Client::class, Invoice::class, InvoiceComing::class, Order::class,
ProductType::class, Product::class, Warehouse::class, Supplier::class, User::class], version = 1)
@TypeConverters(Converters::class)
abstract class SkladDatabase: RoomDatabase() {
    abstract fun skladDAO(): SkladDao
}
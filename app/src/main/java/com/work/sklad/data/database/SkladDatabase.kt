package com.work.sklad.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.work.sklad.data.dao.SkladDao
import com.work.sklad.data.model.*
import com.work.sklad.data.utils.Converters

@Database(entities = [Arrival::class, Expenditure::class, FlowComposition::class, ExportInvoiceComposition::class,
InventoryBalance::class, ArriveInvoiceComposition::class, Product::class, Warehouse::class,
    Organization::class, Authorization::class], version = 1)
@TypeConverters(Converters::class)
abstract class SkladDatabase: RoomDatabase() {
    abstract fun skladDAO(): SkladDao
}
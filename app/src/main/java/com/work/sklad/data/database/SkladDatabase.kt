package com.work.sklad.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.work.sklad.data.dao.SkladDao
import com.work.sklad.data.model.*

@Database(entities = [Arrival::class, Expenditure::class, FlowComposition::class, FlowTypes::class,
InventoryBalance::class, InvoiceComposition::class, Product::class, Warehouse::class, Organization::class], version = 1)
abstract class SkladDatabase: RoomDatabase() {
    abstract fun skladDAO(): SkladDao
}
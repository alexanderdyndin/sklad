package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_balance", foreignKeys = [ForeignKey(
    entity = Product::class,
    parentColumns = arrayOf("articul"),
    childColumns = arrayOf("articul")),
    ForeignKey(
    entity = Warehouse::class,
    parentColumns = arrayOf("number"),
    childColumns = arrayOf("warehouse_number")
)])
data class InventoryBalance(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val articul: Int,
    @ColumnInfo(name = "warehouse_number")
    val warehouseNumber: Int,
    val date: String,
    val count: Int
)

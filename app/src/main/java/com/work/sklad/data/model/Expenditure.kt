package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Product::class,
    parentColumns = arrayOf("articul"),
    childColumns = arrayOf("product")
),  ForeignKey(
    entity = Warehouse::class,
    parentColumns = arrayOf("number"),
    childColumns = arrayOf("warehouse_number")
)])
data class Expenditure(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "limit_number")
    val limitCardNumber: Int,
    @ColumnInfo(name = "warehouse_number")
    val warehouseNumber: Int,
    val department: String,
    val product: Int,
    val month: String,
    val year: Int,
    val limit: Int
)

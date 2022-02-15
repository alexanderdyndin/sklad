package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "invoice_coming",
    foreignKeys = [ForeignKey(
        entity = Warehouse::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("warehouse_id")
    ),
        ForeignKey(
        entity = Supplier::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("supplier_id")
    )]
)
data class InvoiceComing(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val price: Double,
    val count: Int,
    val manufactureDate: LocalDate,
    val expirationDate: LocalDate,
    @ColumnInfo(name = "warehouse_id")
    val warehouseId: Int,
    @ColumnInfo(name = "supplier_id")
    val supplierId: Int,
)

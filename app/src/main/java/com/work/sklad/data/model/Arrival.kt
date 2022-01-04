package com.work.sklad.data.model

import androidx.room.*
import java.time.LocalDate

@Entity(foreignKeys = [ForeignKey(entity = Warehouse::class, parentColumns = ["number"], childColumns = ["warehouse_number"]),
    ForeignKey(entity = Organization::class, parentColumns = ["number"], childColumns = ["supplier"])])
data class Arrival(
    @PrimaryKey
    @ColumnInfo(name = "number_invoice")
    val numberInvoice: Int,
    val date: LocalDate,
    val typeInvoice: String,
    @ColumnInfo(name = "warehouse_number")
    val warehouseNumber: Int,
    val supplier: Int
)

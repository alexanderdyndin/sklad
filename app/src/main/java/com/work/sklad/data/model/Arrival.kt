package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Arrival(
    @PrimaryKey
    @ColumnInfo(name = "number_invoice")
    val numberInvoice: Int,
    val date: String,
    val typeInvoice: String,
    val numberWarehouse: Int,
    val supplier: String
)

package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "invoice_composition", foreignKeys = [ForeignKey(entity = Product::class, parentColumns = arrayOf("articul"), childColumns = arrayOf("articul")),
    ForeignKey(entity = Arrival::class, parentColumns = arrayOf("number_invoice"), childColumns = arrayOf("number_invoice"))])
data class InvoiceComposition(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "number_invoice")
    val numberInvoice: Int,
    val articul: Int,
    val count: Int
)

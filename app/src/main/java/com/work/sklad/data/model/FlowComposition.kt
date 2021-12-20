package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "flow_composition", foreignKeys = [ForeignKey(entity = Expenditure::class, parentColumns = arrayOf("invoice_number"), childColumns = arrayOf("invoice_number"))])
data class FlowComposition(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "invoice_number")
    val invoiceNumber: Int,
    val date: String,
    val count: Int
)

package com.work.sklad.data.model

import androidx.room.*
import com.work.sklad.data.utils.Converters
import java.util.*

@Entity(tableName = "flow_composition", foreignKeys = [ForeignKey(entity = Expenditure::class, parentColumns = arrayOf("invoice_number"), childColumns = arrayOf("invoice_number"))])
data class FlowComposition(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "invoice_number")
    val invoiceNumber: Int,
    val date: Date,
    val count: Int
)

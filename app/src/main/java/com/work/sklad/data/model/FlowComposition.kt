package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "flow_composition", foreignKeys = [ForeignKey(entity = Expenditure::class, parentColumns = arrayOf("limit_number"), childColumns = arrayOf("limit_number"))])
data class FlowComposition(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val numberPP: Int,
    @ColumnInfo(name = "limit_number")
    val limitCardNumber: Int,
    val date: String,
    val count: Int,
    val type: String
)

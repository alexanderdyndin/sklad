package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity(
    foreignKeys = [ForeignKey(
        entity = Warehouse::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("warehouse_id")
    )]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val price: Double,
    val count: Int,
    val manufactureDate: LocalDate,
    val expirationDate: LocalDate,
    @ColumnInfo(name = "warehouse_id")
    val warehouseId: Int
) : Serializable

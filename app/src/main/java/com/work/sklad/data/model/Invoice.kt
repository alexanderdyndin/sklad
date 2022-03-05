package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(entity = Order::class, parentColumns = arrayOf("id"), childColumns = arrayOf("order_id"))
    ]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val manufactureDate: LocalDate,
    val expirationDate: LocalDate,
    @ColumnInfo(name = "order_id")
    val orderId: Int?
) : Serializable

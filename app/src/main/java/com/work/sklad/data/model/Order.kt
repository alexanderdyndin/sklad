package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity(foreignKeys = [
    ForeignKey(entity = Client::class, parentColumns = arrayOf("id"), childColumns = arrayOf("client_id")),
    ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("user_id")),
    ForeignKey(entity = Warehouse::class, parentColumns = arrayOf("id"), childColumns = arrayOf("warehouse_id"))
])
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: LocalDate,
    @ColumnInfo(name = "client_id")
    val clientId: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    val isCompleted: Boolean,
    val isCreated: Boolean,
    val price: Double,
    val count: Int,
    @ColumnInfo(name = "warehouse_id")
    val warehouseId: Int
) : Serializable

package com.work.sklad.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_balance", foreignKeys = [ForeignKey(
    entity = Product::class,
    parentColumns = arrayOf("articul"),
    childColumns = arrayOf("articul")
)])
data class InventoryBalance(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val articul: Int,
    val date: String,
    val count: Int
)

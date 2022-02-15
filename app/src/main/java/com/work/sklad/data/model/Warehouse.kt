package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Product::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("product_id")
)]
)
data class Warehouse(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    @ColumnInfo(name = "product_id")
    val productId: Int,
    @ColumnInfo(name = "free_place")
    val freePlace: Int
)

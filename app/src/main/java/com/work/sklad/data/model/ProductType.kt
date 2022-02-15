package com.work.sklad.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_type")
data class ProductType(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: String
)

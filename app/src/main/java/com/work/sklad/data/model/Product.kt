package com.work.sklad.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val articul: Int,
    val name: String,
    val unit: String,
    val price: Double
)

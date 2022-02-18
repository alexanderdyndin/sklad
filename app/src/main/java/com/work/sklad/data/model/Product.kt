package com.work.sklad.data.model

import androidx.room.*
import java.io.Serializable

@Entity(foreignKeys = [ForeignKey(
    entity = ProductType::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("product_type_id")
)]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val unit: String,
    val price: Double,
    @ColumnInfo(name = "product_type_id")
    val productTypeId: Int
): Serializable

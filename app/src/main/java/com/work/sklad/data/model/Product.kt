package com.work.sklad.data.model

import androidx.room.*
import com.work.sklad.data.utils.Converters
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = Authorization::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("user_id")
)]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val articul: Int,
    val name: String,
    val unit: String,
    val price: Double,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    val expires: Date
)

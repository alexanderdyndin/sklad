package com.work.sklad.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Warehouse(
    @PrimaryKey(autoGenerate = true)
    val number: Int,
    val name: String,
    val warehouseman: String
)

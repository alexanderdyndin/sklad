package com.work.sklad.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flow_types")
data class FlowTypes(
    @PrimaryKey(autoGenerate = true)
    val numberPP: Int,
    val name: String
)

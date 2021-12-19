package com.work.sklad.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Organization(
    @PrimaryKey(autoGenerate = true)
    val number: Int,
    val name: String
)

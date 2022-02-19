package com.work.sklad.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val company: String,
    val email: String,
    val phone: String
): Serializable

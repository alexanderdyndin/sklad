package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.work.sklad.data.utils.Converters

@Entity
data class Authorization(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val password: String,
    @ColumnInfo(name = "user_type")
    val userType: UserType
)

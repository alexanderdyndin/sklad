package com.work.sklad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.work.sklad.data.utils.Converters
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val password: String,
    val name: String,
    val surname: String,
    val patronymic: String?,
    val phone: String,
    @ColumnInfo(name = "user_type")
    val userType: UserType
) : Serializable

package com.work.sklad.data.utils

import androidx.room.TypeConverter
import com.work.sklad.data.model.UserType

class Converters {

    @TypeConverter
    fun toUserType(value: String) = enumValueOf<UserType>(value)

    @TypeConverter
    fun fromUserType(value: UserType) = value.name
}
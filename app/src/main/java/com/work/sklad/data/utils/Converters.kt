package com.work.sklad.data.utils

import androidx.room.TypeConverter
import com.work.sklad.data.model.UserType
import com.work.sklad.utils.format
import com.work.sklad.utils.toLocalDate
import java.time.LocalDate
import java.util.*

class Converters {

    @TypeConverter
    fun toUserType(value: String) = enumValueOf<UserType>(value)

    @TypeConverter
    fun fromUserType(value: UserType) = value.name

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return value.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.toLocalDate()
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.format()
    }

}
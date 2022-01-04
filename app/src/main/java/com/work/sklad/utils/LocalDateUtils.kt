package com.work.sklad.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val localDatePattern = "dd/MM/yyyy"

fun LocalDate.format(pattern: String = localDatePattern): String =
    format(DateTimeFormatter.ofPattern(pattern))

fun String.toLocalDate(pattern: String = localDatePattern): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))
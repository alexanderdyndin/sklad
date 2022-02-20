package com.work.sklad.feature.common.utils

fun Any?.isNull() = this == null

fun Any?.isNotNull() = !isNull()

fun String.limitedChangeListener(limit: Int): String {
    return if (this.length > limit) this.dropLast(this.length - limit) else this
}
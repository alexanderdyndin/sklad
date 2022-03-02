package com.work.sklad.feature.common.utils

import android.text.TextUtils

fun Any?.isNull() = this == null

fun Any?.isNotNull() = !isNull()

fun String.limitedChangeListener(limit: Int): String {
    return if (this.length > limit) this.dropLast(this.length - limit) else this
}

fun String.isEmail() = !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
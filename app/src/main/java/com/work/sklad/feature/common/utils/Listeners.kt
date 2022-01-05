package com.work.sklad.feature.common.utils

typealias Listener = () -> Unit
typealias TypedListener<T> = (T) -> Unit
typealias Typed2Listener<T, M> = (T, M) -> Unit
typealias Typed3Listener<T, M, V> = (T, M, V) -> Unit
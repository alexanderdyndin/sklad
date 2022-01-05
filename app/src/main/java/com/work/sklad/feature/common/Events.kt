package com.work.sklad.feature.common

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Events {
    private val eventFlow = MutableSharedFlow<Event>(replay = 0)
    private val scope = CoroutineScope(Dispatchers.Default)

    fun send(event: Event) {
        scope.launch {
            eventFlow.emit(event)
        }
    }

    fun getFlow() = eventFlow.asSharedFlow()
}

interface Event
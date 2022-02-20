package com.work.sklad.feature.common.base

import android.util.Log
import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.work.sklad.data.dao.SkladDao
import com.work.sklad.domain.repository.ISharedPreferencesRepository
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.Screens
import com.work.sklad.feature.main_activity.ShowMessage
import com.work.sklad.feature.main_activity.ShowNavSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel<TState, TMutator : BaseMutator<TState>, TAction>(
    initialState: TState,
    private val mutator: TMutator
): ViewModel(), StateStore<TState, TMutator> {

    @Inject
    lateinit var sharedPreferences: ISharedPreferencesRepository

    @Inject
    lateinit var skladDao: SkladDao

    @Inject
    lateinit var events: Events

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            state.collect {
                Log.d("STATE", it.toString())
            }
        }
    }

    private val _action = MutableSharedFlow<TAction>(replay = 0)
    val action = _action.asSharedFlow()

    fun action(action: TAction) {
        viewModelScope.launch(Dispatchers.Default) {
            _action.emit(action)
        }
    }

    override fun getMutator(state: TState) = mutator.apply { this.state = state }

    override fun mutateState(mutate: TMutator.() -> Unit) {
        _state.value = getMutator(_state.value).apply(mutate).state
    }

    protected fun message(msg: String) {
        events.send(ShowMessage(msg))
    }

    protected fun message(msg: String, @IdRes destination: Int) {
        events.send(ShowNavSnackbar(msg, destination))
    }

    protected fun closeBottom() {
        events.send(CloseBottom)
    }
}
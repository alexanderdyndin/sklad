package com.work.sklad.feature.clients

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.Client
import com.work.sklad.feature.clients.ClientAction.*
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.login.LoginAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(): BaseViewModel<ClientState, ClientMutator, ClientAction>(ClientState(), ClientMutator()) {

    fun init() {
        viewModelScope.launch {
            skladDao.getClients().collectLatest {
                mutateState { setClients(it) }
            }
        }
    }

    fun registration(company: String, email: String, phone: String) {
        viewModelScope.launch {
            skladDao.addClient(company, email, phone)
        }
    }

    fun openBottom() {
        action(OpenBottom)
    }
}

data class ClientState(
    val clients: List<Client> = emptyList()
)

class ClientMutator: BaseMutator<ClientState>() {

    fun setClients(clients: List<Client>) {
        state = state.copy(clients = clients)
    }

}

sealed class ClientAction {
    object OpenBottom : ClientAction()

}
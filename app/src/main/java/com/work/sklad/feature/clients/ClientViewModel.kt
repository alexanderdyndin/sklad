package com.work.sklad.feature.clients

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.Client
import com.work.sklad.data.model.ProductType
import com.work.sklad.data.model.UserType
import com.work.sklad.data.model.UserType.*
import com.work.sklad.domain.model.ClientDiscount
import com.work.sklad.feature.clients.ClientAction.*
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.common.utils.isEmail
import com.work.sklad.feature.login.LoginAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(): BaseViewModel<ClientState, ClientMutator, ClientAction>(ClientState(), ClientMutator()) {

    fun init() {
        viewModelScope.launch {
            skladDao.getClientsWithDiscount().collectLatest {
                mutateState { setClients(it) }
            }
        }
    }

    fun add(company: String, email: String, phone: String) {
        if (!email.isEmail()) {
            message("Email не валиден")
            return
        }
        state.value.clients.forEach {
            if (it.company == company) {
                message("Компания с таким именем уже существует")
                return
            }
        }
        viewModelScope.launch {
            skladDao.addClient(company, email, phone)
            closeBottom()
        }
    }

    fun updateRequest(client: ClientDiscount) {
        openBottom(client.toClient())
    }

    fun update(client: Client) {
        viewModelScope.launch {
            try{
                skladDao.update(client)
                closeBottom()
            } catch(e: Throwable) {

            }
        }
    }

    fun delete(client: ClientDiscount) {
        when (getUserType()) {
            Admin, WarehouseManager, SalesManager -> {
                viewModelScope.launch {
                    try{
                        skladDao.delete(client.toClient())
                    } catch(e: Throwable) {

                    }
                }
            }
            else -> message("У вас нет прав для этой операции")
        }

    }

    fun openBottom(client: Client? = null) {
        when (getUserType()) {
            Admin, WarehouseManager, SalesManager -> action(OpenBottom(client))
            else -> message("У вас нет прав для этой операции")
        }
    }
}

data class ClientState(
    val clients: List<ClientDiscount> = emptyList(),
    val search: String = ""
)

class ClientMutator: BaseMutator<ClientState>() {

    fun setClients(clients: List<ClientDiscount>) {
        state = state.copy(clients = clients)
    }

    fun setText(text: String) {
        state = state.copy(search = text)
    }

}

sealed class ClientAction {
    data class OpenBottom(val client: Client? = null) : ClientAction()

}
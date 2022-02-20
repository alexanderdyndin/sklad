package com.work.sklad.feature.login

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.Strings
import com.work.sklad.feature.common.Strings.EmptyFieldError
import com.work.sklad.feature.common.UserId
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.login.LoginAction.*
import com.work.sklad.feature.main_activity.ShowMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): BaseViewModel<LoginState, LoginMutator, LoginAction>(LoginState(), LoginMutator()) {

    fun init() {
        val id = sharedPreferences.get(UserId, -1)
        if (id != -1) {
            viewModelScope.launch {
                skladDao.searchUser(id).also {
                    action(Proceed)
                }
            }
        } else {
            mutateState { setLoading(false) }
        }
    }

    fun authorize(login: String, password: String, rememberUser: Boolean = state.value.rememberUser) {
        viewModelScope.launch(Dispatchers.IO) {
            skladDao.searchUser(login, password).also {
                if (it.isNotEmpty()) {
                    val user = it.first()
                    if (rememberUser) {
                        sharedPreferences.set(UserId, user.id)
                    }
                    action(Proceed)
                } else {
                    events.send(ShowMessage(Strings.NoUser))
                }
            }

        }
    }

    fun registration() {
        action(OpenBottomSheet)
    }

    fun register(login: String, password: String, userType: UserType, name: String, surname: String, patronymic: String, phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (login.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || phone.isEmpty()) {
                    message(EmptyFieldError)
                    return@launch
                }
                skladDao.searchUser(login).also {
                    if (it.isEmpty()) {
                        skladDao.addUser(login, password, userType, name, surname, if (patronymic.isNotEmpty()) patronymic else null, phone)
                        closeBottom()
                        message("Регистрация успешна")
                    } else {
                        message("Пользователь с таким логином уже существует")
                    }
                }
            } catch (exception: Throwable) {

            }
        }
    }

}

data class LoginState(
    val rememberUser: Boolean = true,
    val isLoading: Boolean = true,
)

class LoginMutator : BaseMutator<LoginState>() {
    fun setRememberState(remember: Boolean) {
        state = state.copy(rememberUser = remember)
    }
    fun setLoading(loading: Boolean) {
        state = state.copy(isLoading = loading)
    }
}

sealed class LoginAction {
    object OpenBottomSheet : LoginAction()
    object Proceed : LoginAction()
}
package com.work.sklad.feature.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.Screens
import com.work.sklad.feature.common.Strings
import com.work.sklad.feature.common.UserId
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.main_activity.ShowMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): BaseViewModel<LoginState, LoginMutator, LoginAction>(LoginState(), LoginMutator()) {

    fun init() {
        val id = sharedPreferences.get(UserId, -1)
        if (id != -1) {
            viewModelScope.launch {
                skladDao.searchUser(id).collectLatest {
                    Log.e("id" , id.toString())
                }
            }
        }
    }

    fun authorize(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            skladDao.searchUser(login, password).collectLatest {
                if (it.isNotEmpty()) {
                    val user = it.first()
                    if (state.value.rememberUser) {
                        sharedPreferences.set(UserId, user.id)
                    }
                    withContext(Dispatchers.Main) {
                        navigate(Screens.Menu)
                    }
                } else {
                    events.send(ShowMessage(Strings.NoUser))
                }
            }

        }
    }

    fun register(login: String, password: String, userType: UserType) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                skladDao.addUser(login, password, userType)
            } catch (exception: Throwable) {

            }
        }
    }

}

data class LoginState(
    val login: String = "",
    val password: String = "",
    val rememberUser: Boolean = false,
    val userId: Int? = null,
    val loading: Boolean = false
)

class LoginMutator : BaseMutator<LoginState>() {
    fun setRememberState(remember: Boolean) {
        state = state.copy(rememberUser = remember)
    }
}

sealed class LoginAction {
    data class ShowMessage(val message: String)
}
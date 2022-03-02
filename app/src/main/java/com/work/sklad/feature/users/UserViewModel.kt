package com.work.sklad.feature.users

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.User
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.UserId
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.main_activity.ShowMessage
import com.work.sklad.feature.users.UserAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(): BaseViewModel<UserState, UserMutator, UserAction>(UserState(), UserMutator()) {

    fun init() {
        viewModelScope.launch {
            skladDao.getUsers().collectLatest {
                mutateState { setUsers(it) }
            }
        }
    }

    fun addUser(login: String, password: String, userType: UserType, name: String, surname: String, patronymic: String, phone: String) {
        viewModelScope.launch {
            skladDao.addUser(login, password, userType, name, surname, patronymic, phone)
            closeBottom()
        }
    }

    fun deleteUser(User: User) {
        val id = sharedPreferences.get(UserId, -1)
        if (User.id == id) {
            message("Вы не можете удалить самого себя")
            return
        }
        viewModelScope.launch {
            try {
                skladDao.delete(User)
            } catch (e: Throwable){
                events.send(ShowMessage(e.localizedMessage.orEmpty()))
            }
        }
    }

    fun updateUserRequest(User: User) {
        openBottom(User)
    }

    fun updateUser(User: User) {
        viewModelScope.launch {
            try {
                skladDao.update(User)
                closeBottom()
            } catch (e: Throwable){
                events.send(ShowMessage(e.localizedMessage.orEmpty()))
            }
        }
    }

    fun openBottom(User: User? = null) {
        action(OpenBottom(User))
    }
}

data class UserState(
    val Users: List<User> = emptyList(),
)

class UserMutator: BaseMutator<UserState>() {

    fun setUsers(Users: List<User>) {
        state = state.copy(Users = Users)
    }

}

sealed class UserAction {
    data class OpenBottom(val User: User?) : UserAction()

}
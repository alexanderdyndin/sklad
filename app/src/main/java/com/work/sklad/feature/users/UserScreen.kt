package com.work.sklad.feature.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.work.sklad.data.model.Supplier
import com.work.sklad.data.model.User
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.compose.views.ButtonView
import com.work.sklad.feature.common.compose.views.DropDownChangeDelete
import com.work.sklad.feature.common.compose.views.EditText
import com.work.sklad.feature.common.compose.views.Spinner
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener

@Composable
fun UserScreen(viewModel: UserViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.Users) {
            UserItem(it, {viewModel.deleteUser(it)}, {viewModel.updateUserRequest(it)})
        }
    }
}

@Composable
fun UserItem(user: User, onDelete: Listener, onUpdate: Listener) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { expanded = true }) {
        Text(text = "${user.surname} ${user.name} ${user.patronymic.orEmpty()}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Логин: ${user.username}")
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Пароль: ${user.password}")
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Телефон: ${user.phone}")
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Тип учетной записи: ${user.userType}")
        DropDownChangeDelete(expanded = expanded, onDelete = onDelete, onEdit = onUpdate) {
            expanded = false
        }
    }
}

@Composable
fun UserEdit(user: User, registration: TypedListener<Event>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var login by rememberSaveable { mutableStateOf(user.username) }
        var password by rememberSaveable { mutableStateOf(user.password) }
        var name by rememberSaveable { mutableStateOf(user.name) }
        var surname by rememberSaveable { mutableStateOf(user.surname) }
        var patronymic by rememberSaveable { mutableStateOf(user.patronymic.orEmpty()) }
        var phone by rememberSaveable { mutableStateOf(user.phone) }
        var userType by rememberSaveable { mutableStateOf(user.userType) }
        EditText(value = login, label = "Логин"){ login = it }
        EditText(value = password, label = "Пароль"){ password = it }
        EditText(value = name, label = "Имя"){ name = it }
        EditText(value = surname, label = "Фамилия"){ surname = it }
        EditText(value = patronymic, label = "Отчество"){ patronymic = it }
        EditText(value = phone, label = "Телефон"){ phone = it }
        Spinner(stateList = UserType.values(), initialState = userType, nameMapper = { it.toString() }) {
            userType = it
        }
        ButtonView(text = "Редактировать") {
            registration.invoke(EditUserEvent(user.copy(username = login, password = password, name = name, surname = surname, patronymic = patronymic, phone = phone, userType = userType)))
        }
    }
}




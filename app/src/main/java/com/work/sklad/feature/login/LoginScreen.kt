package com.work.sklad.feature.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.compose.views.ButtonView
import com.work.sklad.feature.common.compose.views.EditText
import com.work.sklad.feature.common.compose.views.Spinner
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.Typed3Listener
import com.work.sklad.feature.common.utils.TypedListener
import com.work.sklad.feature.common.utils.limitedChangeListener

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        AuthContent(
            onLoginClick = {login, password, checked -> viewModel.authorize(login, password, checked)},
            registration = { viewModel.registration() },
            rememberUser = { viewModel.mutateState { setRememberState(it) } }
        )
    }
}

@Composable
fun AuthContent(onLoginClick: Typed3Listener<String, String, Boolean>, registration: Listener, rememberUser: TypedListener<Boolean>) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var checked by rememberSaveable { mutableStateOf(true) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = login,
            onValueChange = {
                login = it.limitedChangeListener(15)
            },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(.8f)
        )
        Spacer(modifier = Modifier.padding(5.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it.limitedChangeListener(15)
            },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(.8f)
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Row(Modifier) {
            Checkbox(checked = checked, onCheckedChange = {
                checked = it
                rememberUser.invoke(it)})
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Запомнить меня")
        }
        Spacer(modifier = Modifier.padding(5.dp))
        ButtonView(text = "Войти") { onLoginClick.invoke(login, password, checked) }
        Spacer(modifier = Modifier.padding(5.dp))
        ButtonView(text = "Регистрация") { registration.invoke() }
    }
}

@Composable
fun Registration(registration: TypedListener<RegistrationEvent>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var login by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var name by rememberSaveable { mutableStateOf("") }
        var surname by rememberSaveable { mutableStateOf("") }
        var patronymic by rememberSaveable { mutableStateOf("") }
        var phone by rememberSaveable { mutableStateOf("") }
        var userType by rememberSaveable { mutableStateOf(UserType.Picker) }
        EditText(value = login, label = "Логин"){ login = it }
        EditText(value = password, label = "Пароль"){ password = it }
        EditText(value = name, label = "Имя"){ name = it }
        EditText(value = surname, label = "Фамилия"){ surname = it }
        EditText(value = patronymic, label = "Отчество"){ patronymic = it }
        EditText(value = phone, label = "Телефон"){ phone = it }
        Spinner(stateList = UserType.values(), initialState = userType, nameMapper = { it.toString() }) {
            userType = it
        }
        ButtonView(text = "Регистрация") {
            registration.invoke(RegistrationEvent(login, password, userType, name, surname, patronymic, phone))
        }
    }
}




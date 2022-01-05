package com.work.sklad.feature.login

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.Typed2Listener

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    AuthContent(state,
        onLoginClick = {login, password -> viewModel.authorize(login, password)},
        onLoggedClick = {viewModel.navigateBack()})
}

@Composable
fun AuthContent(state: LoginState, onLoginClick: Typed2Listener<String, String>, onLoggedClick: Listener) {
    Scaffold(topBar = {
        TopAppBar(title = {Text("Авторизация", fontSize = 22.sp)},
            navigationIcon = {IconButton(onClick = { onLoggedClick.invoke() }) {Icon(Icons.Filled.ArrowBack, contentDescription = "Назад") }})
    }) {
//        if (state.auth) {
//            Authorized(state = state) { onLoggedClick.invoke() }
//        } else {
            NotAuthorized(state = state) {login, password -> onLoginClick.invoke(login, password)}
//        }
    }
}

@Composable
fun NotAuthorized(state: LoginState, onAuthorize: Typed2Listener<String,String>) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp).fillMaxWidth()
    ) {
        TextField(
            value = login,
            onValueChange = {
                login = it
            },
            label = { Text("Логин") }
        )
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Пароль") }
        )
        Button(onClick = { onAuthorize.invoke(login, password) }) {
            Text(text = "Войти")
        }
    }
}
@Composable
fun Authorized(state: LoginState, listener: Listener) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp).fillMaxWidth()
    ) {
        Text(text = "Привет")
        Button(onClick = { listener.invoke() }) {
            Text(text = "Перейти к меню")
        }
    }
}


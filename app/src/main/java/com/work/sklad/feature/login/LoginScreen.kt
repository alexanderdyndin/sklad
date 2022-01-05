package com.work.sklad.feature.login

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.Typed2Listener
import com.work.sklad.feature.common.utils.Typed3Listener
import com.work.sklad.feature.common.utils.isNotNull
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    AuthContent(state,
        onLoginClick = {login, password -> viewModel.authorize(login, password)},
        registration = {login, password, type -> viewModel.register(login,password, type)}
    )
}

@ExperimentalMaterialApi
@Composable
fun AuthContent(state: LoginState, onLoginClick: Typed2Listener<String, String>, registration: Typed3Listener<String, String, UserType>) {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            Registration(registration)
        }
    ) {
        Scaffold(topBar = {
            TopAppBar(title = { Text("Авторизация", fontSize = 22.sp) })
        }) {
            if (state.userId.isNotNull()) {
                Authorized(state = state)
            } else {
                NotAuthorized(state = state, onAuthorize = onLoginClick, registration = {coroutineScope.launch { bottomState.show() } })
            }
        }
    }
}

@Composable
fun NotAuthorized(state: LoginState, onAuthorize: Typed2Listener<String,String>, registration: Listener) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
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
        Button(onClick = { registration.invoke() }) {
            Text(text = "Регистрация")
        }
    }
}
@Composable
fun Authorized(state: LoginState) {

}

@Composable
fun Registration(registration: Typed3Listener<String, String, UserType>) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
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
        Button(onClick = { registration.invoke(login, password, UserType.Picker) }) {
            Text(text = "Регистрация")
        }
    }
}


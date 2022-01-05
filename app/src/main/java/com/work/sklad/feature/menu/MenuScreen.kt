package com.work.sklad.feature.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.work.sklad.feature.common.utils.isNotNull
import com.work.sklad.feature.login.Authorized
import com.work.sklad.feature.login.NotAuthorized
import kotlinx.coroutines.launch

@Composable
fun MenuScreen(viewModel: MenuViewModel = hiltViewModel()) {
    viewModel.state.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = { Text("Авторизация", fontSize = 22.sp) })
    }) {
        Column() {
            Text(text = "Menu")
        }
    }
}
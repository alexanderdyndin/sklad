package com.work.sklad.feature.login

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.work.sklad.feature.Screens

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    Button(onClick = { viewModel.navigate(Screens.Login) }) {
        Text("Login")
    }
}


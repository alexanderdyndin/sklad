package com.work.sklad.feature.menu

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MenuScreen(viewModel: MenuViewModel = hiltViewModel()) {
    viewModel.state.collectAsState()

}
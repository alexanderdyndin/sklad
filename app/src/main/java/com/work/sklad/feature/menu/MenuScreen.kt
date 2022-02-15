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
import kotlinx.coroutines.launch

@Composable
fun MenuScreen(viewModel: MenuViewModel = hiltViewModel()) {
    viewModel.state.collectAsState()
    Column() {
        Text(text = "Menu")
    }
}
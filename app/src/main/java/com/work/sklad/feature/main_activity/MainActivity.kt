package com.work.sklad.feature.main_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.insets.ProvideWindowInsets
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.systemBarsPadding
import com.work.sklad.feature.common.Screens
import com.work.sklad.feature.common.AppTheme
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.utils.isNotNull
import com.work.sklad.feature.login.LoginScreen
import com.work.sklad.feature.menu.MenuScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navController: NavHostController

    @Inject
    lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ProvideWindowInsets {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        val scaffoldState = rememberScaffoldState()
                        val coroutineScope = rememberCoroutineScope()
                        events.getFlow().collectAsState(initial = null).value?.let {
                            when (it) {
                                is ShowMessage -> {
                                    coroutineScope.launch {
                                        val current = scaffoldState.snackbarHostState.currentSnackbarData
                                        if (current?.message != it.text) {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = it.text
                                            )
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                        Scaffold(modifier = Modifier
                            .systemBarsPadding()
                            .imePadding(),
                            scaffoldState = scaffoldState
                        ) {
                            MainContent()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MainContent() {
        NavHost(
            navController,
            startDestination = Screens.Login.route
        ) {
            composable(Screens.Login.route) { LoginScreen() }
            composable(Screens.Menu.route) { MenuScreen() }
        }
    }
}

class ShowMessage(val text: String): Event
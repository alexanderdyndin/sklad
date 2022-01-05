package com.work.sklad.feature.main_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.systemBarsPadding
import com.work.sklad.feature.Screens
import com.work.sklad.feature.common.AppTheme
import com.work.sklad.feature.login.LoginScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ProvideWindowInsets {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        Box(
                            Modifier
                                .systemBarsPadding()
                                .imePadding()
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
        }
    }
}
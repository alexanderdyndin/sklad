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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.systemBarsPadding
import com.work.sklad.R
import com.work.sklad.feature.common.Screens
import com.work.sklad.feature.common.AppTheme
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.utils.isNotNull
import com.work.sklad.feature.login.LoginScreen
import com.work.sklad.feature.menu.MenuScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            events.getFlow().collectLatest {
                when (it) {
                    is ShowMessage -> Toast.makeText(this@MainActivity, it.text, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

class ShowMessage(val text: String): Event
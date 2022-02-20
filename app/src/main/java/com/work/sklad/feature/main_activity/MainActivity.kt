package com.work.sklad.feature.main_activity

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.android.material.snackbar.Snackbar

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
                    is ShowNavSnackbar -> {
                        Snackbar.make(findViewById(R.id.nav_host_fragment_container), it.message, Snackbar.LENGTH_SHORT).apply {
                            setAction("Перейти") { _ ->
                                findNavController(R.id.nav_host_fragment_container).navigate(it.destination)
                            }
                            show()
                        }
                    }
                }
            }
        }
    }
}

class ShowMessage(val text: String): Event
class ShowNavSnackbar(val message: String, @IdRes val destination: Int) : Event
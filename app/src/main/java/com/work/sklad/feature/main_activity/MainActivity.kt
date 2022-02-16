package com.work.sklad.feature.main_activity

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import dagger.hilt.android.AndroidEntryPoint
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
package com.work.sklad.feature.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.work.sklad.R
import com.work.sklad.feature.clients.ClientAction.*
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class ClientFragment: BaseFragment() {

    private val viewModel: ClientViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is AddClientEvent -> viewModel.registration(it.company, it.email, it.phone)
            is EditClientEvent -> viewModel.update(it.client)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    is OpenBottom -> {
                        R.id.action_clientFragment_to_addClientFragment.navigate(it.client?.let { client ->
                            bundleOf("client" to client)
                        })
                    }
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(title = "Клиенты", floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { viewModel.openBottom() }) { Icon(Icons.Filled.Add,"") }
        }) {
            ClientsScreen(viewModel = viewModel)
        }
    }

}
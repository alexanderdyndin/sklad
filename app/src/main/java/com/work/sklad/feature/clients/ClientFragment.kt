package com.work.sklad.feature.clients

import android.os.Bundle
import android.view.View
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
import com.work.sklad.R
import com.work.sklad.feature.clients.ClientAction.*
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.ComposeSearchScreen
import com.work.sklad.feature.common.compose.composeView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@AndroidEntryPoint
class ClientFragment: BaseFragment() {

    private val viewModel: ClientViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is AddClientEvent -> viewModel.add(it.company, it.email, it.phone)
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
        val state by viewModel.state.collectAsState()
        ComposeSearchScreen(hint = "Клиенты", text = state.search, textChange = {viewModel.mutateState { setText(it) }}, floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { viewModel.openBottom() }) { Icon(Icons.Filled.Add,"") }
        }) {
            ClientsScreen(viewModel = viewModel)
        }
    }

}
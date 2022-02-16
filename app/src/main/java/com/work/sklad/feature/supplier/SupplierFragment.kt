package com.work.sklad.feature.supplier

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.work.sklad.R
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
class SupplierFragment: BaseFragment() {

    private val viewModel: SupplierViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is AddSupplierEvent -> viewModel.registration(it.company, it.email, it.phone)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    SupplierAction.OpenBottom -> R.id.action_supplierFragment2_to_addSupplierFragment2.navigate()
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
            SuppliersScreen(viewModel = viewModel)
        }
    }

}
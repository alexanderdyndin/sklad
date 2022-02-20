package com.work.sklad.feature.supplier

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
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.ComposeSearchScreen
import com.work.sklad.feature.common.compose.composeView
import com.work.sklad.feature.supplier.SupplierAction.OpenBottom
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@AndroidEntryPoint
class SupplierFragment: BaseFragment() {

    private val viewModel: SupplierViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is AddSupplierEvent -> viewModel.addSupplier(it.company, it.email, it.phone)
            is EditSupplierEvent -> viewModel.updateSupplier(it.supplier)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    is OpenBottom -> R.id.action_supplierFragment2_to_addSupplierFragment2.navigate(it.supplier?.let { supplier ->
                        bundleOf("supplier" to supplier)
                    })
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        val state by viewModel.state.collectAsState()
        ComposeSearchScreen(hint = "Поставщики", text = state.search, textChange = {viewModel.mutateState { setText(it) }}, floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { viewModel.openBottom() }) { Icon(Icons.Filled.Add,"") }
        }) {
            SuppliersScreen(viewModel = viewModel)
        }
    }

}
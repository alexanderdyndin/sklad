package com.work.sklad.feature.invoice

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import com.work.sklad.feature.invoice.InvoiceAction.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class InvoiceFragment: BaseFragment() {

    private val viewModel: InvoiceViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is AddInvoiceEvent -> viewModel.addInvoice(it.price, it.count, it.manufactureDate, it.expirationDate, it.warehouseId)
            is EditInvoiceEvent -> viewModel.update(it.invoice)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    is OpenBottom -> {
                        val bundle = bundleOf(
                            "warehouses" to it.warehouses.toTypedArray()
                        ).apply {
                            it.invoice?.let { invoice -> putSerializable("invoice", invoice) }
                        }
                        R.id.action_invoiceFragment_to_invoiceAddFragment.navigate(bundle)
                    }
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(title = "Накладная расхода", floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { viewModel.openBottom() }) { Icon(Icons.Filled.Add,"") }
        }) {
            InvoicesScreen(viewModel = viewModel)
        }
    }

}
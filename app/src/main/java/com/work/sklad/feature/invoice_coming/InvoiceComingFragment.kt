package com.work.sklad.feature.invoice_coming

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import com.work.sklad.feature.invoice_coming.InvoiceComingAction.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class InvoiceComingFragment: BaseFragment() {

    private val viewModel: InvoiceComingViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is AddInvoiceComingEvent -> viewModel.addInvoice(it.price, it.count, it.manufactureDate, it.expirationDate, it.warehouseId, it.supplierId)
            is EditInvoiceComingEvent -> viewModel.update(it.invoiceComing)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    is OpenBottom -> {
                        val bundle = bundleOf(
                            "warehouses" to it.warehouses.toTypedArray(),
                            "suppliers" to it.suppliers.toTypedArray()
                        ).apply {
                            it.invoiceComing?.let { invoiceComing ->
                                putSerializable("invoice", invoiceComing)
                            }
                        }
                        R.id.action_invoiceComingFragment_to_invoiceComingAddFragment.navigate(bundle)
                    }
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(title = "Накладная прихода", floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { viewModel.openBottom() }) { Icon(Icons.Filled.Add,"") }
        }) {
            InvoicesScreen(viewModel = viewModel)
        }
    }

}
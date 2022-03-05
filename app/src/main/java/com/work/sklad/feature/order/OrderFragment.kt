package com.work.sklad.feature.order

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.print.PDFPrint
import android.print.PDFPrint.OnPDFPrintListener
import android.view.View
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tejpratapsingh.pdfcreator.activity.PDFViewerActivity.PDF_FILE_URI
import com.tejpratapsingh.pdfcreator.utils.FileManager
import com.tejpratapsingh.pdfcreator.utils.PDFUtil
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import com.work.sklad.feature.order.OrderAction.*
import com.work.sklad.feature.pdf_activity.PdfActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.lang.Exception

@AndroidEntryPoint
class OrderFragment: BaseFragment() {

    private val viewModel: OrderViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is AddOrderEvent -> viewModel.addInvoice(it.date, it.clientId, it.isCompleted, it.isCreated, it.warehouseId, it.price, it.count)
            is EditOrderEvent -> viewModel.update(it.order)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    is OpenBottom -> {
                        val bundle = bundleOf(
                            "clients" to it.clients.toTypedArray(),
                            "warehouses" to it.warehouses.toTypedArray()
                        ).apply {
                            it.order?.let { order ->
                                putSerializable("order", order)
                            }
                        }
                        R.id.action_orderFragment_to_orderAddFragment.navigate(bundle)
                    }
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(title = "Заказы", floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { viewModel.openBottom() }) { Icon(Icons.Filled.Add,"") }
        }) {
            OrdersScreen(viewModel = viewModel)
        }
    }

}
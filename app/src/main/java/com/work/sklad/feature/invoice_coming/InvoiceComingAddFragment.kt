package com.work.sklad.feature.invoice_coming

import androidx.compose.runtime.Composable
import com.work.sklad.data.model.InvoiceComing
import com.work.sklad.data.model.ProductType
import com.work.sklad.data.model.Supplier
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class InvoiceComingAddFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = {
        AddInvoiceComingScreen(
            requireArguments().getSerializable("invoice") as? InvoiceComing,
            requireArguments().getSerializable("warehouses") as Array<WarehouseWithProduct>,
            requireArguments().getSerializable("suppliers") as Array<Supplier>,
        ) { events.send(it) }
    }

}

data class AddInvoiceComingEvent(val price: Double, val count: Int, val manufactureDate: LocalDate,
                                 val expirationDate: LocalDate, val warehouseId: Int, val supplierId: Int): Event
data class EditInvoiceComingEvent(val invoiceComing: InvoiceComing): Event
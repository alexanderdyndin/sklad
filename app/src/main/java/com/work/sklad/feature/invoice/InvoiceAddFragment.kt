package com.work.sklad.feature.invoice

import androidx.compose.runtime.Composable
import com.work.sklad.data.model.ProductType
import com.work.sklad.data.model.Supplier
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class InvoiceAddFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = {
        AddInvoiceScreen(
            requireArguments().getSerializable("warehouses") as Array<WarehouseWithProduct>
        ) { events.send(it) }
    }

}

data class AddInvoiceEvent(val price: Double, val count: Int, val manufactureDate: LocalDate,
                                 val expirationDate: LocalDate, val warehouseId: Int): Event
package com.work.sklad.feature.order

import androidx.compose.runtime.Composable
import com.work.sklad.data.model.Client
import com.work.sklad.data.model.Order
import com.work.sklad.data.model.ProductType
import com.work.sklad.data.model.Supplier
import com.work.sklad.domain.model.InvoiceWithWarehouse
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class OrderAddFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = {
        AddOrderScreen(
            requireArguments().getSerializable("order") as? Order,
            requireArguments().getSerializable("clients") as Array<Client>,
            requireArguments().getSerializable("warehouses") as Array<WarehouseWithProduct>,
        ) { events.send(it) }
    }

}

data class AddOrderEvent(val date: LocalDate, val clientId: Int, val warehouseId: Int, val price: Double, val count: Int, val isCompleted: Boolean, val isCreated: Boolean): Event
data class EditOrderEvent(val order: Order): Event
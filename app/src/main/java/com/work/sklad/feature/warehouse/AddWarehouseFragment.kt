package com.work.sklad.feature.warehouse

import androidx.compose.runtime.Composable
import com.work.sklad.data.model.Product
import com.work.sklad.data.model.ProductType
import com.work.sklad.data.model.Warehouse
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWarehouseFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = {
        AddWarehouseScreen(
            requireArguments().getSerializable("warehouse") as? Warehouse,
            requireArguments().getSerializable("products") as Array<Product>
        ) {
            events.send(it)
        }
    }

}

data class AddWarehouseEvent(val name: String, val freePlace: Int, val productId: Int) : Event
data class EditWarehouseEvent(val warehouse: Warehouse) : Event
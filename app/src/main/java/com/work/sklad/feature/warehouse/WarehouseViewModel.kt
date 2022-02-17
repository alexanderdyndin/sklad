package com.work.sklad.feature.warehouse

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.Product
import com.work.sklad.data.model.Warehouse
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.main_activity.ShowMessage
import com.work.sklad.feature.warehouse.WarehouseAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WarehouseViewModel @Inject constructor(): BaseViewModel<WarehouseState, WarehouseMutator, WarehouseAction>(WarehouseState(), WarehouseMutator()) {

    fun init() {
        viewModelScope.launch {
//            skladDao.getWarehouses().collectLatest {
//                mutateState { setWarehouses(it) }
//            }
        }
    }

    fun registration(name: String, unit: String, price: Double, typeId: Int) {
        viewModelScope.launch {
//            skladDao.addWarehouse(name, unit, price, typeId)
        }
    }

    fun openBottom() {
        viewModelScope.launch {
            skladDao.getProducts().collectLatest {
                if (it.isEmpty()) {
                    events.send(ShowMessage("Необходимо добавить хотя бы один тип продукта"))
                } else {
                    action(OpenBottom(it))
                }
            }
        }

    }
}

data class WarehouseState(
    val warehouses: List<Warehouse> = emptyList()
)

class WarehouseMutator: BaseMutator<WarehouseState>() {

    fun setWarehouses(warehouses: List<Warehouse>) {
        state = state.copy(warehouses = warehouses)
    }

}

sealed class WarehouseAction {
    data class OpenBottom(val types: List<Product>) : WarehouseAction()
}
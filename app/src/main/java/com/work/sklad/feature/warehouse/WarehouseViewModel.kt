package com.work.sklad.feature.warehouse

import androidx.lifecycle.viewModelScope
import com.work.sklad.R
import com.work.sklad.data.model.Product
import com.work.sklad.data.model.ProductType
import com.work.sklad.data.model.Warehouse
import com.work.sklad.domain.model.ProductWithType
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.Strings
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
            skladDao.getWarehouseWithProduct().collectLatest {
                mutateState { setWarehouses(it) }
            }
        }
    }

    fun add(name: String, freePlace: Int, productId: Int) {
        viewModelScope.launch {
            if (name.isEmpty()) {
                message(Strings.EmptyFieldError)
                return@launch
            }
            skladDao.addWarehouse(name, freePlace, productId)
            closeBottom()
        }
    }

    fun updateRequest(warehouse: WarehouseWithProduct) {
        viewModelScope.launch {
            skladDao.getProductList().also {
                action(OpenBottom(it, warehouse.toWarehouse()))
            }
        }
    }

    fun update(warehouse: Warehouse) {
        viewModelScope.launch {
            try{
                skladDao.update(warehouse)
                closeBottom()
            } catch(e: Throwable) {

            }
        }
    }

    fun delete(warehouse: WarehouseWithProduct) {
        viewModelScope.launch {
            try{
                skladDao.delete(warehouse.toWarehouse())
            } catch(e: Throwable) {

            }
        }
    }

    fun openBottom() {
        viewModelScope.launch {
            skladDao.getProductList().also {
                if (it.isEmpty()) {
                    message("Необходимо добавить хотя бы один товар", R.id.action_global_productFragment)
                } else {
                    action(OpenBottom(it))
                }
            }
        }

    }
}

data class WarehouseState(
    val warehouses: List<WarehouseWithProduct> = emptyList(),
    val search: String = ""
)

class WarehouseMutator: BaseMutator<WarehouseState>() {

    fun setWarehouses(warehouses: List<WarehouseWithProduct>) {
        state = state.copy(warehouses = warehouses)
    }

    fun setText(text: String) {
        state = state.copy(search = text)
    }

}

sealed class WarehouseAction {
    data class OpenBottom(val products: List<Product>, val warehouse: Warehouse? = null) : WarehouseAction()
}
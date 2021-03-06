package com.work.sklad.feature.product_type

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.ProductType
import com.work.sklad.data.model.UserType
import com.work.sklad.data.model.UserType.*
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.product_type.ProductTypeAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductTypeViewModel @Inject constructor(): BaseViewModel<ProductTypeState, ProductTypeMutator, ProductTypeAction>(ProductTypeState(), ProductTypeMutator()) {

    fun init() {
        viewModelScope.launch {
            skladDao.getTypes().collectLatest {
                mutateState { setProductTypes(it) }
            }
        }
    }

    fun add(type: String) {
        state.value.productTypes.forEach {
            if (it.type == type) {
                message("Тип товара с таким именем уже существует")
                return
            }
        }
        viewModelScope.launch {
            skladDao.addTypes(type)
            closeBottom()
        }
    }

    fun updateRequest(productType: ProductType) {
        openBottom(productType)
    }

    fun update(productType: ProductType) {
        viewModelScope.launch {
            try{
                skladDao.update(productType)
                closeBottom()
            } catch(e: Throwable) {

            }
        }
    }

    fun delete(productType: ProductType) {
        when (getUserType()) {
            Admin, WarehouseManager, WarehouseMan -> {}
            else -> {
                message("У вас нет прав для этой операции")
                return
            }
        }
        viewModelScope.launch {
            try{
                skladDao.delete(productType)
            } catch(e: Throwable) {

            }
        }
    }

    fun openBottom(productType: ProductType? = null) {
        when (getUserType()) {
            Admin, WarehouseManager, WarehouseMan -> {}
            else -> {
                message("У вас нет прав для этой операции")
                return
            }
        }
        action(OpenBottom(productType))
    }
}

data class ProductTypeState(
    val productTypes: List<ProductType> = emptyList()
)

class ProductTypeMutator: BaseMutator<ProductTypeState>() {

    fun setProductTypes(productTypes: List<ProductType>) {
        state = state.copy(productTypes = productTypes)
    }

}

sealed class ProductTypeAction {
    data class OpenBottom(val productType: ProductType? = null) : ProductTypeAction()
}
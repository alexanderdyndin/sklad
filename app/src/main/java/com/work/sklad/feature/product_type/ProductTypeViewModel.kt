package com.work.sklad.feature.product_type

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.ProductType
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
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
        viewModelScope.launch {
            skladDao.addTypes(type)
        }
    }

    fun openBottom() {
        action(ProductTypeAction.OpenBottom)
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
    object OpenBottom : ProductTypeAction()
}
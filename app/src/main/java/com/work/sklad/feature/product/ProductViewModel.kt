package com.work.sklad.feature.product

import androidx.lifecycle.viewModelScope
import com.work.sklad.R
import com.work.sklad.data.model.Product
import com.work.sklad.data.model.ProductType
import com.work.sklad.domain.model.ProductWithType
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.main_activity.ShowMessage
import com.work.sklad.feature.product.ProductAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(): BaseViewModel<ProductState, ProductMutator, ProductAction>(ProductState(), ProductMutator()) {

    fun init() {
        viewModelScope.launch {
            skladDao.getProductWithType().collectLatest {
                mutateState { setProducts(it) }
            }
        }
    }

    fun add(name: String, unit: String, typeId: Int) {
        viewModelScope.launch {
            skladDao.addProduct(name, unit, typeId)
            closeBottom()
        }
    }

    fun updateRequest(productWithType: ProductWithType) {
        openBottom(productWithType.toProduct())
    }

    fun update(product: Product) {
        viewModelScope.launch {
            try{
                skladDao.update(product)
                closeBottom()
            } catch(e: Throwable) {

            }
        }
    }

    fun delete(productWithType: ProductWithType) {
        viewModelScope.launch {
            try{
                skladDao.delete(productWithType.toProduct())
            } catch(e: Throwable) {

            }
        }
    }

    fun openBottom(product: Product? = null) {
        viewModelScope.launch {
            skladDao.getProductTypes().also {
                if (it.isEmpty()) {
                    message("Необходимо добавить хотя бы один тип товара", R.id.action_global_productTypeFragment)
                } else {
                    action(OpenBottom(it, product))
                }
            }
        }

    }
}

data class ProductState(
    val products: List<ProductWithType> = emptyList(),
    val search: String = "",
)

class ProductMutator: BaseMutator<ProductState>() {

    fun setProducts(products: List<ProductWithType>) {
        state = state.copy(products = products)
    }

    fun setText(text: String) {
        state = state.copy(search = text)
    }

}

sealed class ProductAction {
    data class OpenBottom(val types: List<ProductType>, val product: Product? = null) : ProductAction()
}
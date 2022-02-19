package com.work.sklad.feature.supplier

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.Supplier
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.main_activity.ShowMessage
import com.work.sklad.feature.supplier.SupplierAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(): BaseViewModel<SupplierState, SupplierMutator, SupplierAction>(SupplierState(), SupplierMutator()) {

    fun init() {
        viewModelScope.launch {
            skladDao.getSuppliers().collectLatest {
                mutateState { setSuppliers(it) }
            }
        }
    }

    fun addSupplier(company: String, email: String, phone: String) {
        viewModelScope.launch {
            skladDao.addSupplier(company, email, phone)
        }
    }

    fun deleteSupplier(supplier: Supplier) {
        viewModelScope.launch {
            try {
                skladDao.deleteSupplier(supplier)
            } catch (e: Throwable){
                events.send(ShowMessage(e.localizedMessage.orEmpty()))
            }
        }
    }

    fun updateSupplier(supplier: Supplier) {
        viewModelScope.launch {
            try {
                skladDao.updateSupplier(supplier)
            } catch (e: Throwable){
                events.send(ShowMessage(e.localizedMessage.orEmpty()))
            }
        }
    }

    fun openBottom() {
        action(OpenBottom)
    }
}

data class SupplierState(
    val suppliers: List<Supplier> = emptyList()
)

class SupplierMutator: BaseMutator<SupplierState>() {

    fun setSuppliers(suppliers: List<Supplier>) {
        state = state.copy(suppliers = suppliers)
    }

}

sealed class SupplierAction {
    object OpenBottom : SupplierAction()

}
package com.work.sklad.feature.supplier

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.Supplier
import com.work.sklad.data.model.UserType
import com.work.sklad.data.model.UserType.*
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.common.utils.isEmail
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
        if (!email.isEmail()) {
            message("Email не валиден")
            return
        }
        state.value.suppliers.forEach {
            if (it.company == company) {
                message("Компания с таким именем уже существует")
                return
            }
        }
        viewModelScope.launch {
            skladDao.addSupplier(company, email, phone)
            closeBottom()
        }
    }

    fun deleteSupplier(supplier: Supplier) {
        when (getUserType()) {
            Admin, WarehouseManager, WarehouseMan -> {}
            else -> {
                message("У вас нет прав для этой операции")
                return
            }
        }
        viewModelScope.launch {
            try {
                skladDao.deleteSupplier(supplier)
            } catch (e: Throwable){
                events.send(ShowMessage(e.localizedMessage.orEmpty()))
            }
        }
    }



    fun updateSupplierRequest(supplier: Supplier) {
        when (getUserType()) {
            Admin, WarehouseManager, WarehouseMan -> {}
            else -> {
                message("У вас нет прав для этой операции")
                return
            }
        }
        openBottom(supplier)
    }

    fun updateSupplier(supplier: Supplier) {
        viewModelScope.launch {
            try {
                skladDao.updateSupplier(supplier)
                closeBottom()
            } catch (e: Throwable){
                events.send(ShowMessage(e.localizedMessage.orEmpty()))
            }
        }
    }

    fun openBottom(supplier: Supplier? = null) {
        when (getUserType()) {
            Admin, WarehouseManager, WarehouseMan -> {}
            else -> {
                message("У вас нет прав для этой операции")
                return
            }
        }
        action(OpenBottom(supplier))
    }
}

data class SupplierState(
    val suppliers: List<Supplier> = emptyList(),
    val search: String = "",
)

class SupplierMutator: BaseMutator<SupplierState>() {

    fun setSuppliers(suppliers: List<Supplier>) {
        state = state.copy(suppliers = suppliers)
    }

    fun setText(text: String) {
        state= state.copy(search = text)
    }

}

sealed class SupplierAction {
    data class OpenBottom(val supplier: Supplier?) : SupplierAction()

}
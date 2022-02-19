package com.work.sklad.feature.invoice

import androidx.lifecycle.viewModelScope
import com.work.sklad.domain.model.InvoiceWithWarehouse
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.invoice.InvoiceAction.*
import com.work.sklad.feature.main_activity.ShowMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(): BaseViewModel<InvoiceState, InvoiceMutator, InvoiceAction>(InvoiceState(), InvoiceMutator()) {

    fun init() {
        viewModelScope.launch {
            skladDao.getInvoice().collectLatest {
                mutateState { setInvoices(it) }
            }
        }
    }

    fun addInvoice(price: Double, count: Int, manufactureDate: LocalDate,
                   expirationDate: LocalDate, warehouseId: Int) {
        viewModelScope.launch {
            skladDao.addInvoice(price, count, manufactureDate, expirationDate, warehouseId)
        }
    }

    fun openBottom() {
        viewModelScope.launch(Dispatchers.IO) {
            val warehouses = skladDao.getWarehouseWithProductList()
            when {
                warehouses.isEmpty() -> events.send(ShowMessage("Добавьте хотя бы один склад"))
                else -> action(OpenBottom(warehouses))
            }
        }
    }

}

data class InvoiceState(
    val invoices: List<InvoiceWithWarehouse> = emptyList()
)

class InvoiceMutator: BaseMutator<InvoiceState>() {

    fun setInvoices(invoices: List<InvoiceWithWarehouse>) {
        state = state.copy(invoices = invoices)
    }

}

sealed class InvoiceAction {
    data class OpenBottom(val warehouses: List<WarehouseWithProduct>) : InvoiceAction()
}
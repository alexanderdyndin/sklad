package com.work.sklad.feature.invoice_coming

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.Supplier
import com.work.sklad.domain.model.InvoiceComingWithWarehouseSupplier
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.invoice_coming.InvoiceComingAction.*
import com.work.sklad.feature.main_activity.ShowMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InvoiceComingViewModel @Inject constructor(): BaseViewModel<InvoiceComingState, InvoiceComingMutator, InvoiceComingAction>(InvoiceComingState(), InvoiceComingMutator()) {

    fun init() {
        viewModelScope.launch {
            skladDao.getInvoiceComing().collectLatest {
                mutateState { setInvoices(it) }
            }
        }
    }

    fun addInvoice(price: Double, count: Int, manufactureDate: LocalDate,
                   expirationDate: LocalDate, warehouseId: Int, supplierId: Int) {
        viewModelScope.launch {
            skladDao.addInvoiceComing(price, count, manufactureDate, expirationDate, warehouseId, supplierId)
        }
    }

    fun openBottom() {
        viewModelScope.launch(Dispatchers.IO) {
            val warehouses = skladDao.getWarehouseWithProductList()
            val suppliers = skladDao.getSuppliersList()
            when {
                warehouses.isEmpty() -> events.send(ShowMessage("Добавьте хотя бы один склад"))
                suppliers.isEmpty() -> events.send(ShowMessage("Добавьте хотя бы одного поставщика"))
                else -> action(OpenBottom(warehouses, suppliers))
            }
        }
    }

}

data class InvoiceComingState(
    val invoices: List<InvoiceComingWithWarehouseSupplier> = emptyList()
)

class InvoiceComingMutator: BaseMutator<InvoiceComingState>() {

    fun setInvoices(invoices: List<InvoiceComingWithWarehouseSupplier>) {
        state = state.copy(invoices = invoices)
    }

}

sealed class InvoiceComingAction {
    data class OpenBottom(val warehouses: List<WarehouseWithProduct>, val suppliers: List<Supplier>) : InvoiceComingAction()
}
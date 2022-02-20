package com.work.sklad.feature.order

import androidx.lifecycle.viewModelScope
import com.work.sklad.R
import com.work.sklad.data.model.Client
import com.work.sklad.data.model.Order
import com.work.sklad.data.model.Supplier
import com.work.sklad.domain.model.InvoiceWithWarehouse
import com.work.sklad.domain.model.OrderWithInvoiceUserClient
import com.work.sklad.domain.model.ProductWithType
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.UserId
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.main_activity.ShowMessage
import com.work.sklad.feature.order.OrderAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(): BaseViewModel<OrderState, OrderMutator, OrderAction>(OrderState(), OrderMutator()) {

    fun init() {
        viewModelScope.launch { skladDao.getOrders().collectLatest { mutateState { setInvoices(it) } } }
    }

    fun addInvoice(date: LocalDate, clientId: Int, invoiceId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            val discount = skladDao.clientHasDiscount(clientId)
            skladDao.addOrder(date, clientId, sharedPreferences.get(UserId, -1), invoiceId, isCompleted)
            if (discount) {
                skladDao.setInvoiceDiscount(invoiceId)
                message("Была добавлена скидка постоянного покупателя 5%")
            }
            closeBottom()
        }
    }

    fun updateRequest(order: OrderWithInvoiceUserClient) {
        viewModelScope.launch(Dispatchers.IO) {
            val clients = skladDao.getClientList()
            val invoice = skladDao.getInvoice(order.invoiceId).first()
            val invoices = skladDao.getFreeInvoices().plus(invoice)
            when {
                clients.isEmpty() -> message("Добавьте хотя бы одного клиента", R.id.action_global_clientFragment)
                invoices.isEmpty() -> message("Добавьте хотя бы одну накладную расхода не использованную в заказе", R.id.action_global_invoiceFragment)
                else -> action(OpenBottom(clients, invoices, order.toOrder()))
            }
        }
    }

    fun update(order: OrderWithInvoiceUserClient) {
        update(order.toOrder())
    }

    fun update(order: Order) {
        viewModelScope.launch {
            try{
                skladDao.update(order)
                closeBottom()
            } catch(e: Throwable) {

            }
        }
    }

    fun delete(productWithType: OrderWithInvoiceUserClient) {
        viewModelScope.launch {
            try{
                skladDao.delete(productWithType.toOrder())
            } catch(e: Throwable) {

            }
        }
    }

    fun openBottom(order: Order? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val clients = skladDao.getClientList()
            val invoices = skladDao.getFreeInvoices()
            when {
                clients.isEmpty() -> message("Добавьте хотя бы одного клиента", R.id.action_global_clientFragment)
                invoices.isEmpty() -> message("Добавьте хотя бы одну накладную расхода не использованную в заказе", R.id.action_global_invoiceFragment)
                else -> action(OpenBottom(clients, invoices, order))
            }
        }
    }

}

data class OrderState(
    val orders: List<OrderWithInvoiceUserClient> = emptyList(),
)

class OrderMutator: BaseMutator<OrderState>() {

    fun setInvoices(orders: List<OrderWithInvoiceUserClient>) {
        state = state.copy(orders = orders)
    }

}

sealed class OrderAction {
    data class OpenBottom(val clients: List<Client>, val invoices: List<InvoiceWithWarehouse>, val order: Order? = null) : OrderAction()
}
package com.work.sklad.feature.order

import androidx.lifecycle.viewModelScope
import com.work.sklad.R
import com.work.sklad.data.model.*
import com.work.sklad.data.model.UserType.*
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

    fun addInvoice(date: LocalDate, clientId: Int, invoiceId: Int, isCompleted: Boolean, isCreated: Boolean) {
        viewModelScope.launch {
            val discount = skladDao.clientHasDiscount(clientId)
            skladDao.addOrder(date, clientId, sharedPreferences.get(UserId, -1), invoiceId, isCompleted, isCreated)
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
        when (getUserType()) {
            Admin, WarehouseManager, SalesManager -> update(order.toOrder())
            else -> { message("У вас нет прав для этой операции") }
        }
    }

    fun updateCompleted(order: OrderWithInvoiceUserClient) {
        when (getUserType()) {
            Admin, WarehouseManager -> update(order.toOrder())
            else -> { message("У вас нет прав для этой операции") }
        }
    }

    fun updateCreated(order: OrderWithInvoiceUserClient) {
        when (getUserType()) {
            Admin, WarehouseManager, Picker -> update(order.toOrder())
            else -> { message("У вас нет прав для этой операции") }
        }
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
        when (getUserType()) {
            Admin, WarehouseManager -> {}
            else -> {
                message("У вас нет прав для этой операции")
                return
            }
        }
        viewModelScope.launch {
            try{
                skladDao.delete(productWithType.toOrder())
            } catch(e: Throwable) {

            }
        }
    }

    fun openBottom(order: Order? = null) {
        when (getUserType()) {
            Admin, WarehouseManager, SalesManager -> {}
            else -> {
                message("У вас нет прав для этой операции")
                return
            }
        }
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

    fun openPdf(order: OrderWithInvoiceUserClient) {
        viewModelScope.launch {
            openPdf(
                createHtml(
                    order,
                    skladDao.getInvoice(order.invoiceId).first()
                )
            )
        }
    }

    private fun createHtml(order: OrderWithInvoiceUserClient, invoice: InvoiceWithWarehouse): String {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>\n" +
                "</title>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "<style>\n" +
                "body {background-color:#ffffff;background-repeat:no-repeat;background-position:top left;background-attachment:fixed;}\n" +
                "h1{font-family:Arial, sans-serif;color:#000000;background-color:#ffffff;}\n" +
                "p {text-align:right;font-family:Georgia, serif;font-size:14px;font-style:normal;font-weight:normal;color:#000000;background-color:#ffffff;}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1></h1>\n" +
                "<p>Кому: ${order.client}</p>\n" +
                "<p>От: ООО Склад </p>\n" +
                "<p>г. Москва, ул. 7-ая Парковая, д. 9/26</p>\n" +
                "<p>т. 8 (768) 561-32-43</p>\n" +
                "<p>Дата ${order.date}</p>\n" +
                "<p style=\"text-align:center;font-family:Georgia, serif;font-size:14px;font-style:normal;font-weight:normal;color:#000000;background-color:#ffffff;\">Накладная расхода № ${order.invoiceId}</p>\n" +
                "<style>\n" +
                "table.GeneratedTable {\n" +
                "  width: 100%;\n" +
                "  background-color: #ffffff;\n" +
                "  border-collapse: collapse;\n" +
                "  border-width: 2px;\n" +
                "  border-color: #000000;\n" +
                "  border-style: solid;\n" +
                "  color: #000000;\n" +
                "}\n" +
                "\n" +
                "table.GeneratedTable td, table.GeneratedTable th {\n" +
                "  border-width: 2px;\n" +
                "  border-color: #000000;\n" +
                "  border-style: solid;\n" +
                "  padding: 3px;\n" +
                "}\n" +
                "\n" +
                "table.GeneratedTable thead {\n" +
                "  background-color: #ffffff;\n" +
                "}\n" +
                "</style>\n" +
                "\n" +
                "<table class=\"GeneratedTable\">\n" +
                "  <thead>\n" +
                "    <tr>\n" +
                "      <th>Склад</th>\n" +
                "      <th>Товар</th>\n" +
                "      <th>Количество</th>\n" +
                "      <th>Единицы измерения</th>\n" +
                "      <th>Произведён</th>\n" +
                "      <th>Срок годности</th>\n" +
                "      <th>Стоимость</th>\n" +
                "    </tr>\n" +
                "  </thead>\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td>${order.warehouse}</td>\n" +
                "      <td>${order.product}</td>\n" +
                "      <td>${invoice.count}</td>\n" +
                "      <td>${invoice.unit}</td>\n" +
                "      <td>${invoice.manufactureDate}</td>\n" +
                "      <td>${invoice.expirationDate}</td>\n" +
                "      <td>${invoice.price}</td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "<p style=\"text-align:left;font-family:Georgia, serif;font-size:14px;font-style:normal;font-weight:normal;color:#000000;background-color:#ffffff;\">ИТОГО СУММА: ${invoice.price}</p>\n" +
                "<p style=\"text-align:left;font-family:Georgia, serif;font-size:14px;font-style:normal;font-weight:normal;color:#000000;background-color:#ffffff;\">Управляющий складом: Паутов Дмитрий Викторович</p>\n" +
                "</body>\n" +
                "</html>"
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
package com.work.sklad.feature.invoice

import androidx.lifecycle.viewModelScope
import com.work.sklad.R
import com.work.sklad.data.model.Invoice
import com.work.sklad.domain.model.InvoiceComingWithWarehouseSupplier
import com.work.sklad.domain.model.InvoiceWithWarehouse
import com.work.sklad.domain.model.OrderWithInvoiceUserClient
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
            val warehouse = skladDao.getWarehouse(warehouseId)
            if (warehouse.getFreePlace() >= count) {
                skladDao.addInvoice(price, count, manufactureDate, expirationDate, warehouseId)
                closeBottom()
            } else {
                message("Нельзя отгрузить товара больше, чем его количество на складе")
            }
        }
    }

    fun updateRequest(invoice: InvoiceWithWarehouse) {
        openBottom(invoice.toInvoice())
    }

    fun update(invoice: Invoice) {
        viewModelScope.launch {
            try{
                skladDao.update(invoice)
                closeBottom()
            } catch(e: Throwable) {

            }
        }
    }

    fun delete(invoice: InvoiceWithWarehouse) {
        viewModelScope.launch {
            try{
                skladDao.delete(invoice.toInvoice())
            } catch(e: Throwable) {

            }
        }
    }

    fun openBottom(invoice: Invoice? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val warehouses = skladDao.getWarehouseWithProductList()
            when {
                warehouses.isEmpty() -> message("Добавьте хотя бы один склад", R.id.action_global_warehouseFragment)
                else -> action(OpenBottom(warehouses, invoice))
            }
        }
    }

    fun openPdf(invoice: InvoiceWithWarehouse) {
        viewModelScope.launch {
            skladDao.getOrder(invoice.id).firstOrNull()?.let { openPdf(createHtml(it, invoice)) } ?: let {
                this@InvoiceViewModel.message("Накладная не связана с заказом")
            }
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

data class InvoiceState(
    val invoices: List<InvoiceWithWarehouse> = emptyList()
)

class InvoiceMutator: BaseMutator<InvoiceState>() {

    fun setInvoices(invoices: List<InvoiceWithWarehouse>) {
        state = state.copy(invoices = invoices)
    }

}

sealed class InvoiceAction {
    data class OpenBottom(val warehouses: List<WarehouseWithProduct>, val invoice: Invoice?) : InvoiceAction()
}
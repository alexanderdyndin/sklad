package com.work.sklad.feature.invoice_coming

import androidx.lifecycle.viewModelScope
import com.work.sklad.R
import com.work.sklad.data.model.InvoiceComing
import com.work.sklad.data.model.Supplier
import com.work.sklad.data.model.UserType
import com.work.sklad.data.model.UserType.*
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
            val warehouse = skladDao.getWarehouse(warehouseId)
            if (warehouse.getFreePlace() >= count) {
                skladDao.addInvoiceComing(
                    price,
                    count,
                    manufactureDate,
                    expirationDate,
                    warehouseId,
                    supplierId
                )
                closeBottom()
            } else {
                message("Нельзя добавить продукта больше, чем количество свободного места на складе")
            }
        }
    }

    fun updateRequest(invoice: InvoiceComingWithWarehouseSupplier) {
        when (getUserType()) {
            Admin, WarehouseManager -> openBottom(invoice.toInvoiceComing())
            else -> message("У вас нет прав для этой операции")
        }
    }

    fun update(invoice: InvoiceComing) {
        viewModelScope.launch {
            try{
                skladDao.update(invoice)
                closeBottom()
            } catch(e: Throwable) {

            }
        }
    }

    fun delete(invoice: InvoiceComingWithWarehouseSupplier) {
        when (getUserType()) {
            Admin, WarehouseManager -> {}
            else -> {
                message("У вас нет прав для этой операции")
                return
            }
        }
        viewModelScope.launch {
            try {
                skladDao.delete(invoice.toInvoiceComing())
            } catch(e: Throwable) {

            }
        }
    }

    fun openBottom(invoiceComing: InvoiceComing? = null) {
        when (getUserType()) {
            Admin, WarehouseManager, WarehouseMan -> {}
            else -> {
                message("У вас нет прав для этой операции")
                return
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val warehouses = skladDao.getWarehouseWithProductList()
            val suppliers = skladDao.getSuppliersList()
            when {
                warehouses.isEmpty() -> message("Добавьте хотя бы один склад", R.id.action_global_warehouseFragment)
                suppliers.isEmpty() -> message("Добавьте хотя бы одного поставщика", R.id.action_global_supplierFragment2)
                else -> action(OpenBottom(warehouses, suppliers, invoiceComing))
            }
        }
    }

    fun openPdf(invoice: InvoiceComingWithWarehouseSupplier) {
        viewModelScope.launch {
            openPdf(
                createHtml(
                    invoice
                )
            )
        }
    }

    private fun createHtml(invoice: InvoiceComingWithWarehouseSupplier): String {
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
                "<p>Кому: ${invoice.company}</p>\n" +
                "<p>От: ООО Склад </p>\n" +
                "<p>г. Москва, ул. 7-ая Парковая, д. 9/26</p>\n" +
                "<p>т. 8 (768) 561-32-43</p>\n" +
                "<p>Дата ${LocalDate.now()}</p>\n" +
                "<p style=\"text-align:center;font-family:Georgia, serif;font-size:14px;font-style:normal;font-weight:normal;color:#000000;background-color:#ffffff;\">Накладная прихода № ${invoice.id}</p>\n" +
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
                "      <td>${invoice.warehouse}</td>\n" +
                "      <td>${invoice.product}</td>\n" +
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

data class InvoiceComingState(
    val invoices: List<InvoiceComingWithWarehouseSupplier> = emptyList()
)

class InvoiceComingMutator: BaseMutator<InvoiceComingState>() {

    fun setInvoices(invoices: List<InvoiceComingWithWarehouseSupplier>) {
        state = state.copy(invoices = invoices)
    }

}

sealed class InvoiceComingAction {
    data class OpenBottom(val warehouses: List<WarehouseWithProduct>, val suppliers: List<Supplier>, val invoiceComing: InvoiceComing? = null) : InvoiceComingAction()
}
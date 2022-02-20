package com.work.sklad.feature.invoice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.work.sklad.data.model.Invoice
import com.work.sklad.domain.model.InvoiceWithWarehouse
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.compose.views.*
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener
import com.work.sklad.feature.main_activity.ShowMessage
import java.time.LocalDate

@Composable
fun InvoicesScreen(viewModel: InvoiceViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.invoices) {
            InvoiceItem(it, {viewModel.delete(it)}) {
                viewModel.updateRequest(it)
            }
        }
    }
}

@Composable
fun InvoiceItem(order: InvoiceWithWarehouse, onDelete: Listener, onUpdate: Listener) {
    var expanded by remember { mutableStateOf(false) }
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = true }
    ) {
        val (col, count) = createRefs()
        Column(modifier = Modifier.constrainAs(col) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
        ) {
            androidx.compose.material3.Text(text = "Накладная №${order.id}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Склад: ${order.warehouse}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Товар: ${order.product}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Количество: ${order.count}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Произведен: ${order.manufactureDate}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Срок годности: ${order.expirationDate}")
            DropDownChangeDelete(expanded = expanded, onDelete = onDelete, onEdit = onUpdate) {
                expanded = false
            }
        }
        androidx.compose.material3.Text(text = "Стоимость: ${order.price}", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.constrainAs(count) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
    }

}

@Composable
fun AddInvoiceScreen(invoice: Invoice?, warehouses: Array<WarehouseWithProduct>, listener: TypedListener<Event>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var count by rememberSaveable { mutableStateOf(invoice?.count?.toString() ?: "0") }
        var price by rememberSaveable { mutableStateOf(invoice?.price?.toString() ?: "0.0") }
        var date1 by remember { mutableStateOf(invoice?.manufactureDate ?: LocalDate.now()) }
        var date2 by remember { mutableStateOf(invoice?.expirationDate ?: LocalDate.now()) }
        var warehouse by rememberSaveable { mutableStateOf(invoice?.warehouseId?.let { warehouses.find { warehouse -> warehouse.id == it } } ?: warehouses.first()) }
        EditText(value = count, label = "Количество", keyboardType = KeyboardType.Number){ count = it }
        EditText(value = price, label = "Цена", keyboardType = KeyboardType.Number){ price = it }
        DatePicker(title = "Изготовлено", date = date1, onDateChange = {date1=it})
        DatePicker(title = "Годен до", date = date2, onDateChange = {date2=it})
        Spinner(name = "Склад", stateList = warehouses, initialState = warehouse, nameMapper = {"${it.name} - ${it.product} -  ${it.getBusyPlace()} ${it.unit}"} ) {
            warehouse = it
        }
        ButtonView(text = invoice?.let { "Редактировать" } ?: "Добавить") {
            try {
                listener.invoke(invoice?.let { EditInvoiceEvent(it.copy(count = count.toInt(),
                    price = price.toDouble(), manufactureDate = date1, expirationDate = date2, warehouseId = warehouse.id)) }
                    ?: AddInvoiceEvent(price.toDouble(),count.toInt(),date1, date2, warehouse.id))
            } catch(e: Throwable) {
                if (e is NumberFormatException) listener.invoke(ShowMessage("Неверный формат цены или количества"))
            }

        }
    }
}
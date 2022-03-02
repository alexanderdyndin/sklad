package com.work.sklad.feature.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.work.sklad.data.model.Client
import com.work.sklad.data.model.Order
import com.work.sklad.domain.model.InvoiceWithWarehouse
import com.work.sklad.domain.model.OrderWithInvoiceUserClient
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.compose.AppTypography
import com.work.sklad.feature.common.compose.views.ButtonView
import com.work.sklad.feature.common.compose.views.DatePicker
import com.work.sklad.feature.common.compose.views.DropDownChangeDelete
import com.work.sklad.feature.common.compose.views.Spinner
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener
import java.time.LocalDate

@Composable
fun OrdersScreen(viewModel: OrderViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.orders) {
            WarehouseItem(it, {viewModel.delete(it)}, {viewModel.updateRequest(it)}, onPdf = {viewModel.openPdf(it)},
                updateChecked = { checked -> viewModel.updateCompleted(it.copy(isCompleted = checked)) }) {
                    checked -> viewModel.updateCreated(it.copy(isCreated = checked))
            }
        }
    }
}

@Composable
fun WarehouseItem(order: OrderWithInvoiceUserClient, onDelete: Listener, onUpdate: Listener, onPdf: Listener, updateChecked: TypedListener<Boolean>, createdChecked: TypedListener<Boolean>) {
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
            androidx.compose.material3.Text(text = "Заказ №${order.id}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Клиент: ${order.client}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Пользователь: ${order.user}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Накладная №${order.invoiceId}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Склад: ${order.warehouse}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Заказ: ${order.product}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Дата: ${order.date}")
            Spacer(modifier = Modifier.padding(2.dp))
            Row() {
                Checkbox(checked = order.isCompleted, onCheckedChange = updateChecked, modifier = Modifier.padding(end = 5.dp))
                Text(text = "Выполнен")
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Row() {
                Checkbox(checked = order.isCreated, onCheckedChange = createdChecked, modifier = Modifier.padding(end = 5.dp))
                Text(text = "Собран")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                DropdownMenuItem(text = { androidx.compose.material3.Text("Редактировать") }, onClick = {
                    onUpdate.invoke()
                    expanded = false
                })
                DropdownMenuItem(text = { androidx.compose.material3.Text("Удалить") }, onClick = {
                    onDelete.invoke()
                    expanded = false
                })
                DropdownMenuItem(text = { androidx.compose.material3.Text("в Pdf") }, onClick = {
                    onPdf.invoke()
                    expanded = false
                })
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
fun AddOrderScreen(order: Order?, clients: Array<Client>, invoices: Array<InvoiceWithWarehouse>,
                       listener: TypedListener<Event>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var date1 by remember { mutableStateOf(order?.date ?: LocalDate.now()) }
        var client by rememberSaveable { mutableStateOf(order?.clientId?.let { clients.find { client -> client.id == it } } ?: clients.first()) }
        var invoice by rememberSaveable { mutableStateOf(order?.invoiceId?.let { invoices.find { invoice -> invoice.id == it } } ?: invoices.first()) }
        var completed by rememberSaveable { mutableStateOf(order?.isCompleted ?: false) }
        var created by rememberSaveable { mutableStateOf(order?.isCreated ?: false) }
        DatePicker(title = "Дата заказа", date = date1, onDateChange = {date1=it})
        Spinner(name = "Клиент", stateList = clients, initialState = client, nameMapper = {it.company} ) {
            client = it
        }
        Spinner(name = "Накладная", stateList = invoices, initialState = invoice, nameMapper = {"${it.id} - ${it.product} - ${it.count}"} ) {
            invoice = it
        }
        order?.let {
            Row(Modifier.padding(bottom = 5.dp).fillMaxWidth(0.5f)) {
                Checkbox(checked = completed, onCheckedChange = {completed = it}, modifier = Modifier.padding(end = 5.dp))
                Text(text = "Выполнен", color = Color.White)
            }
            Row(Modifier.padding(bottom = 5.dp).fillMaxWidth(0.5f)) {
                Checkbox(checked = created, onCheckedChange = {created = it}, modifier = Modifier.padding(end = 5.dp))
                Text(text = "Собран", color = Color.White)
            }
        }
        ButtonView(text = order?.let { "Редактировать" } ?: "Добавить") {
            listener.invoke(order?.let {
                EditOrderEvent(it.copy(date = date1, clientId = client.id, invoiceId = invoice.id,
                    isCompleted = completed))
            } ?: AddOrderEvent(date1, client.id, invoice.id, isCompleted = false, isCreated = false))
        }
    }
}
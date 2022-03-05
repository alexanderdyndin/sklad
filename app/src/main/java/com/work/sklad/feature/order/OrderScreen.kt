package com.work.sklad.feature.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.work.sklad.data.model.Client
import com.work.sklad.data.model.Order
import com.work.sklad.domain.model.InvoiceWithWarehouse
import com.work.sklad.domain.model.OrderWithInvoiceUserClient
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.compose.views.ButtonView
import com.work.sklad.feature.common.compose.views.DatePicker
import com.work.sklad.feature.common.compose.views.EditText
import com.work.sklad.feature.common.compose.views.Spinner
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener
import com.work.sklad.feature.main_activity.ShowMessage
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
            androidx.compose.material3.Text(text = "Склад: ${order.warehouse}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "Товар: ${order.product}")
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
        androidx.compose.material3.Text(text = order.price?.let { "Стоимость: ${order.price}" } ?: let { "" }, style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.constrainAs(count) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
    }
}

@Composable
fun AddOrderScreen(order: Order?, clients: Array<Client>, warehouses: Array<WarehouseWithProduct>,
                       listener: TypedListener<Event>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var count by rememberSaveable { mutableStateOf(order?.count?.toString() ?: "0") }
        var price by rememberSaveable { mutableStateOf(order?.price?.toString() ?: "0.0") }
        var date1 by remember { mutableStateOf(order?.date ?: LocalDate.now()) }
        var client by rememberSaveable { mutableStateOf(order?.clientId?.let { clients.find { client -> client.id == it } } ?: clients.first()) }
        var completed by rememberSaveable { mutableStateOf(order?.isCompleted ?: false) }
        var created by rememberSaveable { mutableStateOf(order?.isCreated ?: false) }
        var warehouse by rememberSaveable { mutableStateOf(order?.warehouseId?.let { warehouses.find { warehouse -> warehouse.id == it } } ?: warehouses.first()) }
        EditText(value = count, label = "Количество", keyboardType = KeyboardType.Number){ count = it }
        EditText(value = price, label = "Цена", keyboardType = KeyboardType.Number){ price = it }
        DatePicker(title = "Дата заказа", date = date1, onDateChange = {date1=it})
        Spinner(name = "Клиент", stateList = clients, initialState = client, nameMapper = {it.company} ) {
            client = it
        }
        Spinner(name = "Склад", stateList = warehouses, initialState = warehouse, nameMapper = {"${it.name} - ${it.product} -  ${it.getBusyPlace()} ${it.unit}"} ) {
            warehouse = it
        }
        order?.let {
            Row(Modifier.padding(bottom = 5.dp).fillMaxWidth(0.5f)) {
                Checkbox(checked = completed, onCheckedChange = {completed = it}, modifier = Modifier.padding(end = 5.dp))
                Text(text = "Выполнен")
            }
            Row(Modifier.padding(bottom = 5.dp).fillMaxWidth(0.5f)) {
                Checkbox(checked = created, onCheckedChange = {created = it}, modifier = Modifier.padding(end = 5.dp))
                Text(text = "Собран")
            }
        }
        ButtonView(text = order?.let { "Редактировать" } ?: "Добавить") {
            try {
                listener.invoke(order?.let {
                    EditOrderEvent(it.copy(date = date1, clientId = client.id, warehouseId = warehouse.id,
                        isCompleted = completed, isCreated = created, count = count.toInt(), price = price.toDouble()))
                } ?: AddOrderEvent(date1, client.id, isCompleted = false, isCreated = false, count = count.toInt(), price = price.toDouble(), warehouseId = warehouse.id))
            } catch(e: Throwable) {
                if (e is NumberFormatException) listener.invoke(ShowMessage("Неверный формат цены или количества"))
            }

        }
    }
}
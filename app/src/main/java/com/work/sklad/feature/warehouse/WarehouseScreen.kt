package com.work.sklad.feature.warehouse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.work.sklad.data.model.Product
import com.work.sklad.data.model.Warehouse
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.compose.views.DropDownChangeDelete
import com.work.sklad.feature.common.compose.views.EditText
import com.work.sklad.feature.common.compose.views.Spinner
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener

@Composable
fun WarehousesScreen(viewModel: WarehouseViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.warehouses) {
            WarehouseItem(warehouse = it, onDelete = { viewModel.delete(it) }) {viewModel.updateRequest(it)}
        }
    }
}

@Composable
fun WarehouseItem(warehouse: WarehouseWithProduct, onDelete: Listener, onUpdate: Listener) {
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
            Text(text = warehouse.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Вместимость: ${warehouse.place}")
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Занято места: ${warehouse.invoiceIn?.let { it - (warehouse.invoiceOut ?: 0) } ?: 0}")
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Товар : ${warehouse.product}")
            DropDownChangeDelete(expanded = expanded, onDelete = onDelete, onEdit = onUpdate) {
                expanded = false
            }
        }
        Text(text = "Свободное место: ${warehouse.place - (warehouse.invoiceIn?:0) + (warehouse.invoiceOut ?: 0) }", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.constrainAs(count) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
    }

}

@Composable
fun AddWarehouseScreen(warehouse: Warehouse?, products: Array<Product>, Warehouse: TypedListener<Event>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var name by rememberSaveable { mutableStateOf(warehouse?.name.orEmpty()) }
        var place by rememberSaveable { mutableStateOf(warehouse?.freePlace ?: 0) }
        EditText(value = name, label = "Название"){ name = it }
        EditText(value = place.toString(), label = "Вместимость", keyboardType = KeyboardType.Number){ place = it.toInt() }
        var type by rememberSaveable { mutableStateOf(warehouse?.productId?.let { id -> products.find { it.id == id } } ?: products.first()) }
        Spinner(stateList = products, initialState = type, nameMapper = {it.name} ) {
            type = it
        }
        Button(onClick = { Warehouse.invoke(warehouse?.let { EditWarehouseEvent(it.copy(name = name, freePlace = place, productId = type.id)) } ?: AddWarehouseEvent(name, place, type.id)) }) {
            Text(text = warehouse?.let { "Редактировать" } ?: "Добавить")
        }
    }
}



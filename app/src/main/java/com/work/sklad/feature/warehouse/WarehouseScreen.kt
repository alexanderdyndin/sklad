package com.work.sklad.feature.warehouse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.work.sklad.data.model.ProductType
import com.work.sklad.data.model.UserType
import com.work.sklad.domain.model.ProductWithType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.views.EditText
import com.work.sklad.feature.common.base.views.Spinner
import com.work.sklad.feature.common.utils.TypedListener
import com.work.sklad.feature.login.RegistrationEvent

@Composable
fun WarehousesScreen(viewModel: WarehouseViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.warehouses) {
            Text(text = it.toString())
        }
    }
}

@Composable
fun AddWarehouseScreen(products: Array<ProductWithType>, Warehouse: TypedListener<AddWarehouseEvent>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var name by rememberSaveable { mutableStateOf("") }
        var unit by rememberSaveable { mutableStateOf("") }
        var price by rememberSaveable { mutableStateOf(0.00) }
        EditText(value = name, label = "Название"){ name = it }
        EditText(value = unit, label = "Единицы измерения"){ unit = it }
        EditText(value = price.toString(), label = "Цена", keyboardType = KeyboardType.Number){ price = it.toDouble() }
        if (products.isNotEmpty()) {
            var type by rememberSaveable { mutableStateOf(products.first()) }
            Spinner(stateList = products, initialState = type, nameMapper = {it.name} ) {
                type = it
            }
            Button(onClick = { Warehouse.invoke(AddWarehouseEvent(name, unit, price, type.id)) }) {
                Text(text = "Добавить")
            }
        }
    }
}



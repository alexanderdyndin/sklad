package com.work.sklad.feature.invoice_coming

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.work.sklad.data.model.Supplier
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.base.views.DatePicker
import com.work.sklad.feature.common.base.views.EditText
import com.work.sklad.feature.common.base.views.Spinner
import com.work.sklad.feature.common.utils.TypedListener
import java.time.LocalDate

@Composable
fun InvoicesScreen(viewModel: InvoiceComingViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.invoices) {
            Text(text = it.toString())
        }
    }
}

@Composable
fun AddInvoiceComingScreen(warehouses: Array<WarehouseWithProduct>, suppliers: Array<Supplier>,
                       listener: TypedListener<AddInvoiceComingEvent>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var count by rememberSaveable { mutableStateOf(0) }
        var price by rememberSaveable { mutableStateOf(0.0) }
        var date1 by remember { mutableStateOf(LocalDate.now()) }
        var date2 by remember { mutableStateOf(LocalDate.now()) }
        var warehouse by rememberSaveable { mutableStateOf(warehouses.first()) }
        var supplier by rememberSaveable { mutableStateOf(suppliers.first()) }
        EditText(value = count.toString(), label = "Количество", keyboardType = KeyboardType.Number){ count = it.toInt() }
        EditText(value = price.toString(), label = "Цена", keyboardType = KeyboardType.Number){ price = it.toDouble() }
        DatePicker(title = "Изготовлено", date = date1, onDateChange = {date1=it})
        DatePicker(title = "Годен до", date = date2, onDateChange = {date2=it})
        Spinner(stateList = warehouses, initialState = warehouse, nameMapper = {it.name} ) {
            warehouse = it
        }
        Spinner(stateList = suppliers, initialState = supplier, nameMapper = {it.company} ) {
            supplier = it
        }
        Button(onClick = { listener.invoke(AddInvoiceComingEvent(price,count,date1, date2, warehouse.id, supplier.id)) }) {
            Text(text = "Добавить")
        }
    }
}
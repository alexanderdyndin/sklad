package com.work.sklad.feature.product_type

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
import androidx.compose.ui.unit.dp
import com.work.sklad.data.model.ProductType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.compose.views.ButtonView
import com.work.sklad.feature.common.compose.views.DropDownChangeDelete
import com.work.sklad.feature.common.compose.views.EditText
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener

@Composable
fun ProductTypesScreen(viewModel: ProductTypeViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.productTypes) {
            TypeItem(it, {viewModel.delete(it)}) { viewModel.updateRequest(it) }
        }
    }
}

@Composable
fun TypeItem(type: ProductType, onDelete: Listener, onUpdate: Listener) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { expanded = true }) {
        Text(text = type.type, style = MaterialTheme.typography.titleLarge)
        DropDownChangeDelete(expanded = expanded, onDelete = onDelete, onEdit = onUpdate) {
            expanded = false
        }
    }
}

@Composable
fun AddProductTypeScreen(productType: ProductType?, ProductType: TypedListener<Event>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var type by rememberSaveable { mutableStateOf(productType?.type.orEmpty()) }
        EditText(value = type, label = "Название"){ type = it }
        ButtonView(text = productType?.let { "Редактировать" } ?: "Добавить") {
            ProductType.invoke(productType?.let { EditProductTypeEvent(it.copy(type = type)) }
                ?: AddProductTypeEvent(type))
        }
    }
}



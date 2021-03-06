package com.work.sklad.feature.supplier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.work.sklad.data.model.Supplier
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.compose.views.ButtonView
import com.work.sklad.feature.common.compose.views.DropDownChangeDelete
import com.work.sklad.feature.common.compose.views.EditText
import com.work.sklad.feature.common.compose.views.MaskPhoneTransformation
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener

@Composable
fun SuppliersScreen(viewModel: SupplierViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.suppliers.filter { supplier -> supplier.company.contains(state.search) }) {
            SupplierItem(it, {viewModel.deleteSupplier(it)}, {viewModel.updateSupplierRequest(it)})
        }
    }
}

@Composable
fun SupplierItem(supplier: Supplier, onDelete: Listener, onUpdate: Listener) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { expanded = true }) {
        Text(text = supplier.company, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Email: ${supplier.email}")
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Телефон: ${supplier.phone}")
        DropDownChangeDelete(expanded = expanded, onDelete = onDelete, onEdit = onUpdate) {
            expanded = false
        }
    }
}

@Composable
fun AddSupplierScreen(supplier: Supplier?, listener: TypedListener<Event>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var company by rememberSaveable { mutableStateOf(supplier?.company.orEmpty()) }
        var email by rememberSaveable { mutableStateOf(supplier?.email.orEmpty()) }
        var phone by rememberSaveable { mutableStateOf(supplier?.phone.orEmpty()) }
        EditText(value = company, label = "Компания"){ company = it }
        EditText(value = email, label = "Email"){ email = it }
        EditText(value = phone, label = "Телефон", visualTransformation = MaskPhoneTransformation(), maxChars = 11, onlyDigits = true){ phone = it }
        ButtonView(text = supplier?.let { "Редактировать" } ?: "Добавить") {
            listener.invoke(supplier?.let { EditSupplierEvent(it.copy(company = company, email = email,
                phone = phone)) } ?: AddSupplierEvent(company, email, phone))
        }
    }
}



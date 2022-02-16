package com.work.sklad.feature.supplier

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
import androidx.compose.ui.unit.dp
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.views.EditText
import com.work.sklad.feature.common.base.views.Spinner
import com.work.sklad.feature.common.utils.TypedListener
import com.work.sklad.feature.login.RegistrationEvent

@Composable
fun SuppliersScreen(viewModel: SupplierViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.suppliers) {
            Text(text = it.toString())
        }
    }
}

@Composable
fun AddSupplierScreen(supplier: TypedListener<AddSupplierEvent>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var company by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var phone by rememberSaveable { mutableStateOf("") }
        EditText(value = company, label = "Компания"){ company = it }
        EditText(value = email, label = "Email"){ email = it }
        EditText(value = phone, label = "Телефон"){ phone = it }
        Button(onClick = { supplier.invoke(AddSupplierEvent(company, email, phone)) }) {
            Text(text = "Добавить")
        }
    }
}



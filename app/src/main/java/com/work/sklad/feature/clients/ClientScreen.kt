package com.work.sklad.feature.clients

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
import com.work.sklad.data.model.Client
import com.work.sklad.domain.model.ClientDiscount
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.compose.views.ButtonView
import com.work.sklad.feature.common.compose.views.DropDownChangeDelete
import com.work.sklad.feature.common.compose.views.EditText
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener

@Composable
fun ClientsScreen(viewModel: ClientViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.clients.filter { clientDiscount -> clientDiscount.company.contains(state.search) }) {
            ClientItem(it, { viewModel.delete(it) }) {
                viewModel.updateRequest(it)
            }
        }
    }
}

@Composable
fun ClientItem(client: ClientDiscount, onDelete: Listener, onUpdate: Listener) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { expanded = true }) {
        Text(text = client.company, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Email: ${client.email}")
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Телефон: ${client.phone}")
        if (client.hasDiscount) {
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Постоянный клиент", style = MaterialTheme.typography.bodyMedium)
        }
        DropDownChangeDelete(expanded = expanded, onDelete = onDelete, onEdit = onUpdate) {
            expanded = false
        }
    }
}

@Composable
fun AddClientScreen(clientEntity: Client?, client: TypedListener<Event>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var company by rememberSaveable { mutableStateOf(clientEntity?.company.orEmpty()) }
        var email by rememberSaveable { mutableStateOf(clientEntity?.email.orEmpty()) }
        var phone by rememberSaveable { mutableStateOf(clientEntity?.phone.orEmpty()) }
        EditText(value = company, label = "Компания"){ company = it }
        EditText(value = email, label = "Email"){ email = it }
        EditText(value = phone, label = "Телефон"){ phone = it }
        ButtonView(text = clientEntity?.let { "Редактировать" } ?: "Добавить") {
            client.invoke(clientEntity?.let {
                EditClientEvent(it.copy(company = company, email = email, phone = phone))
            }?: AddClientEvent(company, email, phone))
        }
    }
}



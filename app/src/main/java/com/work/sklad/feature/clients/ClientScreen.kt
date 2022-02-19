package com.work.sklad.feature.clients

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
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.views.EditText
import com.work.sklad.feature.common.base.views.Spinner
import com.work.sklad.feature.common.utils.TypedListener
import com.work.sklad.feature.login.RegistrationEvent

@Composable
fun ClientsScreen(viewModel: ClientViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.clients) {
            ClientItem(it)
        }
    }
}

@Composable
fun ClientItem(client: Client) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(text = client.company, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Email: ${client.email}")
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Телефон: ${client.phone}")
    }
}

@Composable
fun AddClientScreen(client: TypedListener<AddClientEvent>) {
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
        Button(onClick = { client.invoke(AddClientEvent(company, email, phone)) }) {
            Text(text = "Добавить")
        }
    }
}



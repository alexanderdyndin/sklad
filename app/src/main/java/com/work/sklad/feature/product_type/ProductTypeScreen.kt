package com.work.sklad.feature.product_type

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
fun ProductTypesScreen(viewModel: ProductTypeViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.productTypes) {
            Text(text = it.toString())
        }
    }
}

@Composable
fun AddProductTypeScreen(ProductType: TypedListener<AddProductTypeEvent>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var type by rememberSaveable { mutableStateOf("") }
        EditText(value = type, label = "Название"){ type = it }
        Button(onClick = { ProductType.invoke(AddProductTypeEvent(type)) }) {
            Text(text = "Добавить")
        }
    }
}



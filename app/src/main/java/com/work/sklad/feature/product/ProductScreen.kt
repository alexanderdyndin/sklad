package com.work.sklad.feature.product

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
import com.work.sklad.data.model.ProductType
import com.work.sklad.data.model.UserType
import com.work.sklad.domain.model.ProductWithType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.views.DropDownChangeDelete
import com.work.sklad.feature.common.base.views.EditText
import com.work.sklad.feature.common.base.views.Spinner
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener
import com.work.sklad.feature.login.RegistrationEvent

@Composable
fun ProductsScreen(viewModel: ProductViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.products) {
            ProductItem(it, {}) {}
        }
    }
}

@Composable
fun ProductItem(product: ProductWithType, onDelete: Listener, onUpdate: Listener) {
    var expanded by remember { mutableStateOf(false) }
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
        val (col, count) = createRefs()
        Column(modifier = Modifier.constrainAs(col) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
            ) {
            Text(text = product.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Артикул: ${product.id}")
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Тип: ${product.type}")
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Единицы измерения: ${product.unit}")
            Spacer(modifier = Modifier.padding(2.dp))
            DropDownChangeDelete(expanded = expanded, onDelete = onDelete, onEdit = onUpdate) {
                expanded = false
            }
        }
        Text(text = "Остаток: ${product.count}", style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.constrainAs(count) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        })
    }

}

@Composable
fun AddProductScreen(types: Array<ProductType>, product: TypedListener<AddProductEvent>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var name by rememberSaveable { mutableStateOf("") }
        var unit by rememberSaveable { mutableStateOf("") }
        EditText(value = name, label = "Название"){ name = it }
        EditText(value = unit, label = "Единицы измерения"){ unit = it }
        var type by rememberSaveable { mutableStateOf(types.first()) }
        Spinner(stateList = types, initialState = type, nameMapper = {it.type} ) {
            type = it
        }
        Button(onClick = { product.invoke(AddProductEvent(name, unit, type.id)) }) {
            Text(text = "Добавить")
        }
    }
}



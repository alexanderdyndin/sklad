package com.work.sklad.feature.invoice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.work.sklad.data.model.Invoice
import com.work.sklad.domain.model.InvoiceWithWarehouse
import com.work.sklad.domain.model.OrderWithInvoiceUserClient
import com.work.sklad.domain.model.WarehouseWithProduct
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.compose.views.*
import com.work.sklad.feature.common.utils.Listener
import com.work.sklad.feature.common.utils.TypedListener
import com.work.sklad.feature.main_activity.ShowMessage
import java.time.LocalDate

@Composable
fun InvoicesScreen(viewModel: InvoiceViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.invoices) {
            InvoiceItem(it, {viewModel.delete(it)}, { viewModel.updateRequest(it) }, {viewModel.openPdf(it)})
        }
    }
}

@Composable
fun InvoiceItem(order: InvoiceWithWarehouse, onDelete: Listener, onUpdate: Listener, onPdf: Listener) {
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
            androidx.compose.material3.Text(text = "?????????????????? ???${order.id}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "??????????: ${order.warehouse}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "??????????: ${order.product}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "????????????????????: ${order.count} ${order.unit}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "????????????????????: ${order.manufactureDate}")
            Spacer(modifier = Modifier.padding(2.dp))
            androidx.compose.material3.Text(text = "???????? ????????????????: ${order.expirationDate}")
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                DropdownMenuItem(text = { androidx.compose.material3.Text("??????????????????????????") }, onClick = {
                    onUpdate.invoke()
                    expanded = false
                })
                DropdownMenuItem(text = { androidx.compose.material3.Text("??????????????") }, onClick = {
                    onDelete.invoke()
                    expanded = false
                })
                DropdownMenuItem(text = { androidx.compose.material3.Text("?? Pdf") }, onClick = {
                    onPdf.invoke()
                    expanded = false
                })
            }
        }
        androidx.compose.material3.Text(text = "??????????????????: ${order.price}", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.constrainAs(count) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
    }

}

@Composable
fun AddInvoiceScreen(invoice: Invoice?, warehouses: Array<OrderWithInvoiceUserClient>, listener: TypedListener<Event>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        var date1 by remember { mutableStateOf(invoice?.manufactureDate ?: LocalDate.now()) }
        var date2 by remember { mutableStateOf(invoice?.expirationDate ?: LocalDate.now()) }
        var order by rememberSaveable { mutableStateOf(invoice?.orderId?.let { warehouses.find { warehouse -> warehouse.id == it } } ?: warehouses.first()) }
        DatePicker(title = "??????????????????????", date = date1, onDateChange = {date1=it})
        DatePicker(title = "?????????? ????", date = date2, onDateChange = {date2=it})
        Spinner(name = "??????????", stateList = warehouses, initialState = order, nameMapper = {"${it.client} - ${it.product} - ${it.count}"} ) {
            order = it
        }
        ButtonView(text = invoice?.let { "??????????????????????????" } ?: "????????????????") {
            try {
                listener.invoke(invoice?.let { EditInvoiceEvent(it.copy(manufactureDate = date1, expirationDate = date2, orderId = order.id)) }
                    ?: AddInvoiceEvent(date1, date2, order.id))
            } catch(e: Throwable) {
                if (e is NumberFormatException) listener.invoke(ShowMessage("???????????????? ???????????? ???????? ?????? ????????????????????"))
            }

        }
    }
}
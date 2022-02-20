package com.work.sklad.feature.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.work.sklad.data.model.User
import com.work.sklad.feature.common.utils.Listener

@Composable
fun MenuScreen(viewModel: MenuViewModel) {
    val state by viewModel.state.collectAsState()
    viewModel.init()
    MenuContent(
        state = state,
        onWarehouseClick = { viewModel.navigateWarehouse() },
        onInvoiceClick = { viewModel.navigateInvoice() },
        onInvoiceComingClick = { viewModel.navigateInvoiceComing() },
        onProductClick = { viewModel.navigateProduct() },
        onProductTypeClick = { viewModel.navigateProductType() },
        onSupplierClick = { viewModel.navigateSupplier() },
        onClientClick = { viewModel.navigateClient() },
        onOrderClick = { viewModel.navigateOrder() }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MenuContent(
    state: MenuState,
    onWarehouseClick: Listener,
    onInvoiceClick: Listener,
    onInvoiceComingClick: Listener,
    onProductClick: Listener,
    onProductTypeClick: Listener,
    onSupplierClick: Listener,
    onClientClick: Listener,
    onOrderClick: Listener,
) {
    Column(
        Modifier
            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .height(190.dp)
                .fillMaxWidth()
        ) {
            Column {
                FirstCard(onInvoiceComingClick)
                SecondCard(onInvoiceClick)
            }
            state.user?.let {
                UserCard(it)
            }
        }
        Row(
            Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            CommonCard1("Склады", onWarehouseClick)
            CommonCard2("Заказы", onOrderClick)
        }
        Row(
            Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            CommonCard1("Товары", onProductClick)
            CommonCard2("Типы товаров", onProductTypeClick)
        }
        Row(
            Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            CommonCard1("Поставщики", onSupplierClick)
            CommonCard2("Клиенты", onClientClick)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FirstCard(
    onClick: Listener
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(70.dp)
            .fillMaxWidth(0.6f),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = "Приход")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SecondCard(
    onClick: Listener
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(120.dp)
            .fillMaxWidth(0.6f),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = "Расход")
            Spacer(Modifier.height(4.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserCard(
    user: User
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = user.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            Text(
                text = user.surname,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            user.patronymic?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = user.userType.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommonCard1(
    title: String,
    onClick: Listener
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxHeight()
            .fillMaxWidth(0.5f),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = title)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommonCard2(
    title: String,
    onClick: Listener
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = title)
        }
    }
}
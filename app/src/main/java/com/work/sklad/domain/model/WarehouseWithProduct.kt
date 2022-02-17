package com.work.sklad.domain.model

import com.work.sklad.data.model.Product

data class WarehouseWithProduct(
    val id: Int,
    val name: String,
    val freePlace: Int,
    val product: Product
)
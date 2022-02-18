package com.work.sklad.domain.model

import com.work.sklad.data.model.Product
import java.io.Serializable

data class WarehouseWithProduct(
    val id: Int,
    val name: String,
    val freePlace: Int,
    val product: String,
    val type: String
) : Serializable
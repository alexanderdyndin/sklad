package com.work.sklad.domain.model

import com.work.sklad.data.model.Product
import java.io.Serializable

data class WarehouseWithProduct(
    val id: Int,
    val name: String,
    val place: Int,
    val freePlace: Int,
    val busyPlace: Int,
    val product: String,
    val type: String
) : Serializable
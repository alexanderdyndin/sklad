package com.work.sklad.domain.model

import com.work.sklad.data.model.Product
import com.work.sklad.data.model.Warehouse
import java.io.Serializable

data class WarehouseWithProduct(
    val id: Int,
    val name: String,
    val place: Int,
    val invoiceIn: Long?,
    val invoiceOut: Long?,
    val productId: Int,
    val product: String,
    val type: String
) : Serializable {
    fun toWarehouse(): Warehouse = Warehouse(id, name, productId, place)
}
package com.work.sklad.domain.model

import com.work.sklad.data.model.InvoiceComing
import java.time.LocalDate

data class InvoiceComingWithWarehouseSupplier(
    val id: Int,
    val product: String,
    val warehouseId: Int,
    val warehouse: String,
    val price: Double,
    val count: Int,
    val manufactureDate: LocalDate,
    val expirationDate: LocalDate,
    val supplierId: Int,
    val company: String
) {
    fun toInvoiceComing() = InvoiceComing(id, price, count, manufactureDate, expirationDate, warehouseId, supplierId)
}
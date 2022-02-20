package com.work.sklad.domain.model

import com.work.sklad.data.model.Invoice
import java.io.Serializable
import java.time.LocalDate

data class InvoiceWithWarehouse(
    val id: Int,
    val product: String,
    val warehouseId: Int,
    val warehouse: String,
    val price: Double,
    val count: Int,
    val manufactureDate: LocalDate,
    val expirationDate: LocalDate,
    val unit: String
): Serializable {
    fun toInvoice() = Invoice(id, price, count, manufactureDate, expirationDate, warehouseId)
}
package com.work.sklad.domain.model

import com.work.sklad.data.model.Order
import java.io.Serializable
import java.time.LocalDate

data class OrderWithInvoiceUserClient(
    val id: Int,
    val date: LocalDate,
    val clientId: Int,
    val client: String,
    val userId: Int,
    val user: String,
    val invoiceId: Int,
    val price: Double,
    val warehouse: String,
    val product: String,
    val isCompleted: Boolean,
    val isCreated: Boolean
): Serializable {
    fun toOrder() = Order(id, date, clientId, userId, invoiceId, isCompleted, isCreated)
}
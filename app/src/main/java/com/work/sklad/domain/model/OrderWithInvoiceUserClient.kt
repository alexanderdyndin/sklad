package com.work.sklad.domain.model

import java.io.Serializable
import java.time.LocalDate

data class OrderWithInvoiceUserClient(
    val id: Int,
    val date: LocalDate,
    val client: String,
    val user: String,
    val invoiceId: Int,
    val price: Double,
    val warehouse: String,
    val product: String,
    val isCompleted: Boolean
): Serializable
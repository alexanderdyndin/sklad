package com.work.sklad.domain.model

import java.io.Serializable
import java.time.LocalDate

data class InvoiceWithWarehouse(
    val id: Int,
    val product: String,
    val warehouse: String,
    val price: Double,
    val count: Int,
    val manufactureDate: LocalDate,
    val expirationDate: LocalDate
): Serializable
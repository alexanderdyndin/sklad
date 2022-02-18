package com.work.sklad.domain.model

import java.time.LocalDate

data class InvoiceComingWithWarehouseSupplier(
    val id: Int,
    val product: String,
    val warehouse: String,
    val price: Double,
    val count: Int,
    val manufactureDate: LocalDate,
    val expirationDate: LocalDate,
    val company: String
)
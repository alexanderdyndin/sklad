package com.work.sklad.domain.model

import com.work.sklad.data.model.Client

data class ClientDiscount(
    val id: Int,
    val company: String,
    val email: String,
    val phone: String,
    val hasDiscount: Boolean
) {
    fun toClient() = Client(id, company, email, phone)
}

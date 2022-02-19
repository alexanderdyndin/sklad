package com.work.sklad.domain.model

import com.work.sklad.data.model.Product
import com.work.sklad.data.model.ProductType
import java.io.Serializable

data class ProductWithType(
    val id: Int,
    val name: String,
    val unit: String,
    val typeId: Int,
    val type: String,
    val count: Int
): Serializable {
    fun toProduct() = Product(id, name, unit, typeId)
}
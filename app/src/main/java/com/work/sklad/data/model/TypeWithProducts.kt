package com.work.sklad.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.work.sklad.data.model.ProductType
import com.work.sklad.domain.model.ProductWithType

data class TypeWithProducts(
    @Embedded
    val type: ProductType,
    @Relation(
        parentColumn = "id",
        entityColumn = "product_type_id"
    )
    val products: List<Product>
)

fun TypeWithProducts.toProductWithType(): List<ProductWithType> {
    return products.map { ProductWithType(it.id, it.name, it.unit, it.price, type) }
}

fun List<TypeWithProducts>.toProductWithType(): List<ProductWithType> {
    val list = mutableListOf<ProductWithType>()
    forEach {
        list.addAll(it.toProductWithType())
    }
    return list
}
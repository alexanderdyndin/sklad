package com.work.sklad.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.work.sklad.data.model.ProductType
import com.work.sklad.domain.model.ProductWithType

data class TypeWithWarehouses(
    @Embedded
    val product: TypeWithProducts,
    @Relation(
        parentColumn = "id",
        entityColumn = "product_id"
    )
    val products: List<Warehouse>
)

//fun TypeWithWarehouses.toWarehouseWithProduct(): List<ProductWithType> {
//    return products.map { ProductWithType(it.id, it.name, it.unit, it.price, type) }
//}
//
//fun List<TypeWithWarehouses>.toProductWithType(): List<ProductWithType> {
//    val list = mutableListOf<ProductWithType>()
//    forEach {
//        list.addAll(it.toProductWithType())
//    }
//    return list
//}
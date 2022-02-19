package com.work.sklad.domain.model

import com.work.sklad.data.model.ProductType

data class ProductWithType(
    val id: Int,
    val name: String,
    val unit: String,
    val type: String,
    val count: Int
)
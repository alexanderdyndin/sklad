package com.work.sklad.feature.product_type

import androidx.compose.runtime.Composable
import com.work.sklad.data.model.ProductType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductTypeFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = { AddProductTypeScreen(requireArguments().getSerializable("type") as? ProductType) { events.send(it) } }

}

data class AddProductTypeEvent(val type: String) : Event
data class EditProductTypeEvent(val type: ProductType) : Event
package com.work.sklad.feature.product

import androidx.compose.runtime.Composable
import com.work.sklad.data.model.ProductType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = {
        AddProductScreen(requireArguments().getSerializable("types") as Array<ProductType>) {
            events.send(it) }
    }

}

data class AddProductEvent(val name: String, val unit: String, val typeId: Int) : Event
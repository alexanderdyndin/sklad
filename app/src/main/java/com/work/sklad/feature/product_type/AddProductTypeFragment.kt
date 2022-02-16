package com.work.sklad.feature.product_type

import androidx.compose.runtime.Composable
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductTypeFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = { AddProductTypeScreen { events.send(it) } }

}

data class AddProductTypeEvent(val type: String) : Event
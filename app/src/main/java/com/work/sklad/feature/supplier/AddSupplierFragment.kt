package com.work.sklad.feature.supplier

import androidx.compose.runtime.Composable
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSupplierFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = { AddSupplierScreen { events.send(it) } }

}

data class AddSupplierEvent(val company: String, val email: String, val phone: String) : Event
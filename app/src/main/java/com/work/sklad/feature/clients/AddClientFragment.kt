package com.work.sklad.feature.clients

import androidx.compose.runtime.Composable
import com.work.sklad.data.model.Client
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddClientFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = { AddClientScreen(requireArguments().getSerializable("client") as? Client) { events.send(it) } }

}

data class AddClientEvent(val company: String, val email: String, val phone: String) : Event
data class EditClientEvent(val client: Client) : Event
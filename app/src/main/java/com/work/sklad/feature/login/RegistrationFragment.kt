package com.work.sklad.feature.login

import androidx.compose.runtime.Composable
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = { Registration(registration = { events.send(it) }) }

}

data class RegistrationEvent(val login: String, val password: String, val userType: UserType,
                             val name: String, val surname: String, val patronymic: String?,
                             val phone: String) : Event
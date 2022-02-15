package com.work.sklad.feature.login

import android.os.Bundle
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.base.BaseBottomSheet
import com.work.sklad.feature.common.compose.ComposeBottomScreen
import com.work.sklad.feature.common.compose.composeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment: BaseBottomSheet() {

    @Inject
    lateinit var events: Events

    override fun view() = composeView(requireContext()) {
        ComposeBottomScreen {
            Registration(registration = { events.send(it) })
        }
    }

}

data class RegistrationEvent(val login: String, val password: String, val userType: UserType,
                             val name: String, val surname: String, val patronymic: String?,
                             val phone: String) : Event
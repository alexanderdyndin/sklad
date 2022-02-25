package com.work.sklad.feature.users

import androidx.compose.runtime.Composable
import com.work.sklad.data.model.User
import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserEditFragment: BaseBottomSheet() {

    override val composable: @Composable (() -> Unit) = { UserEdit(requireArguments().getSerializable("user") as User, registration = { events.send(it) }) }

}

data class EditUserEvent(val user: User) : Event
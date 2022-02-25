package com.work.sklad.feature.users

import android.os.Bundle
import android.view.View
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import com.work.sklad.feature.users.UserAction.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@AndroidEntryPoint
class UserFragment: BaseFragment() {

    private val viewModel: UserViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is EditUserEvent -> viewModel.updateUser(it.user)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    is OpenBottom -> R.id.action_userFragment_to_userEditFragment.navigate(it.User?.let { user ->
                        bundleOf("user" to user)
                    })
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(title = "Пользователи") {
            UserScreen(viewModel = viewModel)
        }
    }

}
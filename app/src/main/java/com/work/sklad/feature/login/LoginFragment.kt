package com.work.sklad.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.work.sklad.R
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class LoginFragment: BaseFragment() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    LoginAction.OpenBottomSheet -> findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
                    LoginAction.Proceed -> findNavController().navigate(R.id.action_global_menuFragment)
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            events.getFlow().collectLatest {
                with (it) {
                    when (this) {
                        is RegistrationEvent -> {
                            viewModel.register(login, password, userType, name, surname, patronymic, phone)
                        }
                    }
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(title = "Авторизация") {
            LoginScreen(viewModel)
        }
    }

}
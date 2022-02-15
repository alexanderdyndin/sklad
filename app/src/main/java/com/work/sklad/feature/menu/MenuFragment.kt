package com.work.sklad.feature.menu

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
class MenuFragment: BaseFragment() {

    private val viewModel: MenuViewModel by viewModels()

    override val backPressAction: (() -> Unit) = {
        requireActivity().finish()
    }

    @Inject
    lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {

                }
            }
        }
        lifecycleScope.launchWhenCreated {
            events.getFlow().collectLatest {
                with (it) {
                    when (this) {

                    }
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(title = "Авторизация") {
            MenuScreen(viewModel)
        }
    }

}
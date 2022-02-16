package com.work.sklad.feature.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class MenuFragment: BaseFragment() {

    private val viewModel: MenuViewModel by viewModels()

    var backPressedToExit = false

    override val backPressAction: (() -> Unit) = {
        lifecycleScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                if (backPressedToExit) {
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show()
                    backPressedToExit = true
                }
            }
            delay(5000)
            backPressedToExit = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    MenuAction.Client -> R.id.clientFragment.navigate()
                    MenuAction.Invoice -> TODO()
                    MenuAction.InvoiceComing -> TODO()
                    MenuAction.Order -> TODO()
                    MenuAction.Product -> TODO()
                    MenuAction.ProductType -> TODO()
                    MenuAction.Supplier -> TODO()
                    MenuAction.Warehouse -> TODO()
                    MenuAction.Logout -> back()
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(
            title = "Меню",
            actions = {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = "", modifier = Modifier.clickable {
                    viewModel.logOut()
                })
            }
        ) {
            MenuScreen(viewModel)
        }
    }

}